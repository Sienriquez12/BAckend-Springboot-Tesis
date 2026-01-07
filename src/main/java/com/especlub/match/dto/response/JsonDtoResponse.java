package com.especlub.match.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Respuesta JSON estándar para operaciones del API.
 * Incluye estado HTTP, mensaje, resultado y código de error.
 *
 * @param <T> Tipo de dato retornado en el campo {@code result}.
 */
@ToString
@Data
@Schema(description = "Respuesta estándar JSON utilizada en todas las operaciones del API.")
public class JsonDtoResponse<T> {

    @Schema(description = "Código de estado HTTP asociado a la respuesta", example = "200")
    private int statusCode;

    @Schema(description = "Mensaje descriptivo del result", example = "Operación realizada con éxito")
    private String message;

    @Schema(description = "Génerico con los datos devueltos por la operación. Puede ser nulo si no aplica")
    private T result;


    private JsonDtoResponse(String message, int statusCode, T result) {
        this.statusCode = statusCode;
        this.message = message;
        this.result = result;
    }


    public ResponseEntity<JsonDtoResponse<T>> toResponseEntity() {
        return ResponseEntity.status(statusCode).body(this);
    }

    /******************************** RESPUESTAS DE EXITO ******************************/
    // La solicitud se procesó correctamente y devuelve datos.
    public static <T> JsonDtoResponse<T> ok(String message) {
        return new JsonDtoResponse<>(message, HttpStatus.OK.value(), null);
    }

    public static <T> JsonDtoResponse<T> ok(String message, T result) {
        return new JsonDtoResponse<>(message, HttpStatus.OK.value(), result);
    }

    // Se creó un recurso nuevo (usuario, solicitud de firma, carpeta, etc).
    public static <T> JsonDtoResponse<T> created(String message, T result) {
        return new JsonDtoResponse<>(message, HttpStatus.CREATED.value(), result);
    }

    // La solicitud fue aceptada, pero se procesará más tarde (ej. firma asincrónica).
    public static <T> JsonDtoResponse<T> accepted(String message, T result) {
        return new JsonDtoResponse<>(message, HttpStatus.ACCEPTED.value(), result);
    }

    // La operación fue exitosa, pero no hay cuerpo de respuesta (ej. borrado).
    public static <T> JsonDtoResponse<T> noContent() {
        return new JsonDtoResponse<>(null, HttpStatus.NO_CONTENT.value(), null);
    }


    /******************************** RESPUESTAS DE ERROR ******************************/
    // Datos inválidos (campos faltantes, formato incorrecto, JSON mal formado).
    public static <T> JsonDtoResponse<T> badRequest(String message) {
        return new JsonDtoResponse<>(message, HttpStatus.BAD_REQUEST.value(), null);
    }

    public static <T> JsonDtoResponse<T> badRequest(String message, T result) {
        return new JsonDtoResponse<>(message, HttpStatus.BAD_REQUEST.value(), result);
    }

    // Error de autenticación (credenciales inválidas, token expirado).
    public static <T> JsonDtoResponse<T> unauthorized(String message) {
        return new JsonDtoResponse<>(message, HttpStatus.UNAUTHORIZED.value(), null);
    }

    // El usuario no tiene permiso para acceder al recurso (ej. acceso a una carpeta privada).
    public static <T> JsonDtoResponse<T> forbidden(String message) {
        return new JsonDtoResponse<>(message, HttpStatus.FORBIDDEN.value(), null);
    }

    // El recurso solicitado no existe (ej. usuario, carpeta, firma).
    public static <T> JsonDtoResponse<T> notFound(String message) {
        return new JsonDtoResponse<>(message, HttpStatus.NOT_FOUND.value(), null);
    }

    // Conflicto de estado (usuario ya existe, firma duplicada, solicitud ya procesada, etc.).
    public static <T> JsonDtoResponse<T> conflict(String message) {
        return new JsonDtoResponse<>(message, HttpStatus.CONFLICT.value(), null);
    }

    // El servidor no pudo procesar la solicitud debido a un error interno.
    public static <T> JsonDtoResponse<T> internalServerError(String message) {
        return new JsonDtoResponse<>(message, HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
    }

    // No se implementó la operación solicitada (ej. firma de tipo no soportado).
    public static <T> JsonDtoResponse<T> notImplemented(String message) {
        return new JsonDtoResponse<>(message, HttpStatus.NOT_IMPLEMENTED.value(), null);
    }

    // El servidor no pudo procesar la solicitud debido a un error de puerta de enlace.
    public static <T> JsonDtoResponse<T> badGateway(String message) {
        return new JsonDtoResponse<>(message, HttpStatus.BAD_GATEWAY.value(), null);
    }

    // El servidor no pudo procesar la solicitud debido a un error interno.
    public static <T> JsonDtoResponse<T> serviceUnavailable(String message) {
        return new JsonDtoResponse<>(message, HttpStatus.SERVICE_UNAVAILABLE.value(), null);
    }

    // A superado el tiempo de espera al procesar la solicitud.
    public static <T> JsonDtoResponse<T> gatewayTimeout(String message) {
        return new JsonDtoResponse<>(message, HttpStatus.GATEWAY_TIMEOUT.value(), null);
    }

    public static <T> JsonDtoResponse<T> error(String message, int status) {
        return new JsonDtoResponse<>(message, status, null);
    }

    public static <T> JsonDtoResponse<T> error(String message, int status, T result){
        return new JsonDtoResponse<>(message, status, result);
    }
}