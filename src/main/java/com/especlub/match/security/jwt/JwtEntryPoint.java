package com.especlub.match.security.jwt;


import com.especlub.match.dto.response.JsonDtoResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JwtEntryPoint is responsible for handling unauthorized access attempts.
 * It implements AuthenticationEntryPoint to provide a custom response when authentication fails.
 */
@Component
@Slf4j
public class JwtEntryPoint implements AuthenticationEntryPoint {

    private static final String TEXT_NO_AUTHORIZED = "Acceso no autorizado";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException e)
            throws IOException {

        log.error("Unauthorized error: {}", e.getMessage());

        JsonDtoResponse<Void> jsonResponse = JsonDtoResponse.unauthorized(
                TEXT_NO_AUTHORIZED
        );

        res.setContentType("application/json");
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        OBJECT_MAPPER.writeValue(res.getWriter(), jsonResponse);

        res.getWriter().flush();
        res.getWriter().close();
    }
}