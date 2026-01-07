package com.especlub.match.shared.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class CookieUtils {

    @Value("${cookie.domain:}")
    private String cookieDomain; // puede ser vacío en local

    @Value("${cookie.sameSite:Lax}")
    private String sameSite; // Lax por defecto en local

    @Value("${cookie.secure:true}")
    private boolean secure; // configurable

    @Value("${cookie.maxAgeSeconds:10000}")
    private int jwtExpirationSeconds;

    private static final String NAME_COOKIE = "access_token";

    public void addTokenCookie(HttpServletRequest request, HttpServletResponse response, String token) {
        log.debug("Adding token to cookie: {}", token);
        String sameSiteValue = (sameSite == null || sameSite.isBlank()) ? "Lax" : sameSite;
        boolean forceSecure = false;
        if ("None".equalsIgnoreCase(sameSiteValue) && !secure) {
            log.warn("SameSite=None requiere Secure=true. Forzando Secure=true para compatibilidad cross-browser.");
            forceSecure = true;
        }
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(NAME_COOKIE, token)
                .httpOnly(true)
                .secure(secure || forceSecure)
                .path("/")
                .maxAge(jwtExpirationSeconds)
                .sameSite(sameSiteValue);
        if (cookieDomain != null && !cookieDomain.isEmpty()) {
            builder.domain(cookieDomain.trim());
        }
        ResponseCookie cookie = builder.build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public void clearTokenCookie(HttpServletRequest request, HttpServletResponse response) {
        String sameSiteValue = (sameSite == null || sameSite.isBlank()) ? "Lax" : sameSite;
        boolean forceSecure = false;
        if ("None".equalsIgnoreCase(sameSiteValue) && !secure) {
            log.warn("SameSite=None requiere Secure=true. Forzando Secure=true para compatibilidad cross-browser.");
            forceSecure = true;
        }
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(NAME_COOKIE, "")
                .httpOnly(true)
                .secure(secure || forceSecure)
                .path("/")
                .maxAge(0)
                .sameSite(sameSiteValue);
        if (cookieDomain != null && !cookieDomain.isEmpty()) {
            builder.domain(cookieDomain.trim());
        }
        ResponseCookie cookie = builder.build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public String extractTokenFromHeaderOrCookie(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.replace("Bearer ", "");
        }
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if (NAME_COOKIE.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
