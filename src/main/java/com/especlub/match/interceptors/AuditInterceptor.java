package com.especlub.match.interceptors;

import com.especlub.match.models.AuditLog;
import com.especlub.match.models.SystemParameters;
import com.especlub.match.models.UserInfo;
import com.especlub.match.repositories.AuditLogRepository;
import com.especlub.match.repositories.UserInfoRepository;
import com.especlub.match.services.interfaces.SystemParametersService;
import com.especlub.match.shared.enums.CatalogEnums;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.BufferedReader;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditInterceptor implements HandlerInterceptor {
    private final AuditLogRepository auditLogRepository;
    private final UserInfoRepository userInfoRepository;
    private final SystemParametersService systemParametersService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            String endpoint = request.getRequestURI();
            String httpMethod = request.getMethod();
            String ipAddress = request.getRemoteAddr();
            String userAgent = request.getHeader("User-Agent");
            String requestBody;
            if (request instanceof CachedBodyHttpServletRequest cachedRequest) {
                requestBody = extractRequestBody(cachedRequest);
            } else {
                requestBody = extractRequestBody(request);
            }
            String responseBody = null;
            if (response instanceof CachedBodyHttpServletResponse cachedResponse) {
                byte[] bodyBytes = cachedResponse.getCachedBody();
                if (bodyBytes != null && bodyBytes.length > 0) {
                    responseBody = new String(bodyBytes, response.getCharacterEncoding() != null ? response.getCharacterEncoding() : "UTF-8");
                }
            }
            Integer statusCode = response.getStatus();
            String username = (request.getUserPrincipal() != null) ? request.getUserPrincipal().getName() : null;
            UserInfo user = null;
            if (username != null) {
                user = userInfoRepository.findByUsernameAndRecordStatusTrue(username);
            }

            if (!shouldAudit()) {
                log.debug("Audit logging disabled by system parameter '{}'", CatalogEnums.ESTADO_AUDITORIA.getMnemonic());
                return;
            }

            AuditLog auditLog = AuditLog.builder()
                    .user(user)
                    .ipAddress(ipAddress)
                    .httpMethod(httpMethod)
                    .endpoint(endpoint)
                    .requestBody(requestBody)
                    .responseBody(responseBody)
                    .statusCode(statusCode)
                    .actionDate(LocalDateTime.now())
                    .userAgent(userAgent)
                    .build();
            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            log.error("Error saving audit log: {}", e.getMessage());
        }
    }

    private boolean shouldAudit() {
        try {
            SystemParameters param = systemParametersService.getActiveByMnemonic(CatalogEnums.ESTADO_AUDITORIA.getMnemonic());
            return param != null && param.getValue() != null && "1".equals(param.getValue().trim());
        } catch (Exception ex) {
            log.error("Error checking system parameter for audit logging: {}", ex.getMessage());
            return false;
        }
    }

    private String extractRequestBody(HttpServletRequest request) {
        try {
            if ("POST".equalsIgnoreCase(request.getMethod()) || "PUT".equalsIgnoreCase(request.getMethod())) {
                BufferedReader reader = request.getReader();
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        } catch (Exception e) {
            log.debug("Could not extract request body: {}", e.getMessage());
        }
        return null;
    }
}
