package com.especlub.match.security.jwt;

import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.models.SystemParameters;
import com.especlub.match.repositories.SystemParametersRepository;
import com.especlub.match.security.config.UserDetailsServiceImpl;
import com.especlub.match.shared.enums.CatalogEnums;
import com.especlub.match.shared.utils.CookieUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Combined filter for rate limiting and JWT authentication.
 */
@RequiredArgsConstructor
@Slf4j
@Component
@Order(1)
public class RateLimitAndJwtFilter extends OncePerRequestFilter {

    private final SystemParametersRepository parametrosSistemaRepository;
    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final CookieUtils cookieUtils;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();


    private List<String> getWhitelistedIpsFromDB() {
        SystemParameters parametro = parametrosSistemaRepository.findByMnemonicAndRecordStatusTrue(CatalogEnums.WHITE_LISTED_IP.getMnemonic());
        if (parametro == null || parametro.getValue() == null) {
            log.warn("No se encontró configuración activa para WHITE_LISTED_IP");
            return Collections.emptyList();
        }
        try {
            List<String> lista = objectMapper.readValue(parametro.getValue(), new TypeReference<>() {});
            log.debug("IPs en whitelist: {}", lista);
            return lista;
        } catch (Exception e) {
            log.error("Error al convertir la lista de IPs confiables: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private boolean isWhitelisted(String clientIp, List<String> whitelist) {
        return whitelist.contains("0.0.0.0") || whitelist.contains(clientIp);
    }

    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.builder()
                .capacity(10)
                .refillGreedy(50, Duration.ofMinutes(1))
                .build(); // 40 requests per minute
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String clientIp = request.getRemoteAddr();
        List<String> whitelistedIps = getWhitelistedIpsFromDB();

        // Rate limiting logic
        if (!isWhitelisted(clientIp, whitelistedIps)) {
            Bucket bucket = buckets.computeIfAbsent(clientIp, k -> createNewBucket());
            log.debug("IP {} has {} tokens available", clientIp, bucket.getAvailableTokens());
            if (!bucket.tryConsume(1)) {
                log.warn("IP {} blocked due to too many requests", clientIp);
                response.setContentType("application/json");
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                JsonDtoResponse<Object> jsonResponse = JsonDtoResponse.error(
                        "Too many requests. Try again later.",
                        HttpStatus.TOO_MANY_REQUESTS.value(),
                        null
                );
                String json = objectMapper.writeValueAsString(jsonResponse);
                response.getWriter().write(json);
                return;
            }
        } else {
            log.debug("IP {} is whitelisted, skipping rate limit", clientIp);
        }

        // JWT authentication logic
        String path = request.getRequestURI();
        log.info("Incoming request to: {}", path);
        String jwt = getToken(request);
        boolean isJwtValid = jwt != null && jwtProvider.validateToken(jwt);
        if (isJwtValid) {
            String username = jwtProvider.getNombreUsuarioFromToken(jwt);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.info("JWT authentication set for user: {}", username);
        } else {
            log.warn("No valid JWT or API Key found. Access may be denied.");
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        return cookieUtils.extractTokenFromHeaderOrCookie(request);
    }
}
