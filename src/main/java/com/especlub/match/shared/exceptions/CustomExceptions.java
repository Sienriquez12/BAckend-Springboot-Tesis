package com.especlub.match.shared.exceptions;


import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Excepción personalizada para proyectos de Security Data para errores específicos.
 * Incluye un mensaje y un código de estado HTTP (por ejemplo, 400 o 404).
 */
@Getter
public class CustomExceptions extends RuntimeException {

    private final int statusCode;


    public CustomExceptions() {
        super("Error interno del servidor");
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public CustomExceptions(String message) {
        super(message);
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }


    public CustomExceptions(String message, int statusCode) {
        super(message);
        if (statusCode < 100 || statusCode > 599) {
            throw new IllegalArgumentException("HTTP status code inválido: %s".formatted(statusCode));
        }
        this.statusCode = statusCode;
    }

}