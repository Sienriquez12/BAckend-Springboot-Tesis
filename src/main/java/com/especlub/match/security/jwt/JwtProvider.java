package com.especlub.match.security.jwt;

import com.especlub.match.models.UserInfo;
import com.especlub.match.models.UserRole;
import com.especlub.match.security.config.MainUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.especlub.match.shared.exceptions.CustomExceptions;
import org.springframework.http.HttpStatus;

/**
 * JWT Provider for generating and validating JWT tokens.
 * This class provides methods to generate JWT tokens based on user authentication,
 */
@Component
@Slf4j
public class JwtProvider {

    private static final String CLAIM_ROLES = "roles";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    public String generateToken(Authentication authentication) {
        MainUser mainUser = (MainUser) authentication.getPrincipal();
        List<String> roles = mainUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return Jwts.builder().subject(mainUser.getUsername()).claim(CLAIM_ROLES, roles).issuedAt(new Date()).expiration(new Date(new Date().getTime() + expiration * 1000L)).signWith(getSecret(secret)).compact();
    }

    public String generateJwtByUsername(UserInfo userInfo) {
        List<String> roles = userInfo.getRoles()
                .stream()
                .map(UserRole::getName)
                .collect(Collectors.toList());
        return Jwts.builder()
                .subject(userInfo.getUsername())
                .claim(CLAIM_ROLES, roles)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + expiration * 1000L))
                .signWith(getSecret(secret))
                .compact();
    }

    public String getNombreUsuarioFromToken(String token) {
        // Defensive checks to avoid passing null/empty token to the jwt lib which throws IllegalArgumentException
        if (token == null || token.isBlank()) {
            throw new CustomExceptions("Token inválido o ausente", HttpStatus.UNAUTHORIZED.value());
        }
        // validate token first; validateToken already catches and logs specific JWT exceptions
        if (!validateToken(token)) {
            throw new CustomExceptions("Token inválido o expirado", HttpStatus.UNAUTHORIZED.value());
        }
        Jws<Claims> parsed = Jwts.parser().verifyWith(getSecret(secret)).build().parseSignedClaims(token);
        return parsed.getPayload().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSecret(secret)).build().parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty");
        } catch (SignatureException e) {
            log.error("Invalid JWT signature");
        } catch (JwtException e) {
            log.error("Invalid JWT");
        }
        return false;
    }

    private SecretKey getSecret(String secret) {
        byte[] secretBytes = Decoders.BASE64URL.decode(secret);
        return Keys.hmacShaKeyFor(secretBytes);
    }

}
