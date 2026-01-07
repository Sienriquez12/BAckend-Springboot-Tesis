package com.especlub.match.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.UUID;

/**
 * LoggingInterceptorConfig.java
 * This class configures a Spring MVC interceptor that logs request and response details.
 * It implements the HandlerInterceptor interface.
 */
@Slf4j
@Component
public class LoggingInterceptorConfig implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        log.info(":::::::::::::::::::::: START REQUEST ::::::::::::::::::::::");
        Long inicio = System.currentTimeMillis();
        request.setAttribute("inicio", inicio);
        // the correlationId is used to trace requests across different services
        String correlationId = Optional.ofNullable(request.getHeader("X-Correlation-ID"))
                .filter(id -> !id.isEmpty())
                .orElseGet(() -> {
                    String[] parts = UUID.randomUUID().toString().split("-");
                    return parts[0] + parts[1];
                });


        MDC.put("correlationId", correlationId);
        response.setHeader("X-Correlation-ID", correlationId);

        log.info("CorrelationID: {}", correlationId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            log.info("Username: {}", authentication.getName());
        } else {
            log.info("Unauthenticated user");
        }

        log.info("HTTP Method: {} / URL: {}", request.getMethod(), request.getRequestURL().toString());
        return true;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                           @NonNull Object handler,
                           @Nullable ModelAndView modelAndView) {
        log.info("::::::::::::::::::::::::: SUCCESS  REQUEST::::::::::::::::::::::::::");

        if (modelAndView != null) {
            log.info("View: {}", modelAndView.getViewName());
            log.info("Attributes: {}", modelAndView.getModelMap());
        }
    }

    // Runs after the request completes (useful for resource cleanup)
    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable Exception ex) {
        Long fin = System.currentTimeMillis();
        long result = fin - (Long) request.getAttribute("inicio");
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        log.info("Status : {} / MethodClass: {} / TimeRequest: {} ms", response.getStatus(), handlerMethod.getMethod().getName(), result);
        log.info("::::::::::::::::::::::: END REQUEST :::::::::::::::::::::::");
        MDC.clear();
    }
}
