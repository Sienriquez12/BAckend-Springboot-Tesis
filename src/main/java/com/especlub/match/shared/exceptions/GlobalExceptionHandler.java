package com.especlub.match.shared.exceptions;


import com.especlub.match.dto.response.JsonDtoResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.*;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import javax.security.auth.login.AccountLockedException;
import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private Integer stateCode;
    private String message;

    // esta clase se usa para mandar una excepción personalizada, cuando no encontramos una excepción específica
    @ExceptionHandler(CustomExceptions.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleCustomException(CustomExceptions ex) {
        log.warn ("ERROR PERSONALIZADO ===>", ex);
        message = (ex.getMessage() != null && !ex.getMessage().isBlank())
                ? ex.getMessage()
                : "Se produjo un error personalizado en la operación";
        return JsonDtoResponse.error(message, ex.getStatusCode(), null).toResponseEntity();
    }
    // esta clase se usa para mandar una excepción personalizada, cuando no encontramos una excepción específica

    // ---------------------- ERRORES DE INFRAESTRUCTURA / RED  ----------------------
    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<JsonDtoResponse<Object>> conecException(ConnectException ex, HttpServletRequest request) {
        log.warn("CONEXIÓN FALLIDA ===>", ex);
        stateCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        message = "Error de conexión ";
        return JsonDtoResponse.error(message, stateCode).toResponseEntity();
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleResourceAccessException(ResourceAccessException ex) {
        log.warn("ERROR DE ACCESO A RECURSO ===>", ex);
        message = "No se pudo acceder al recurso solicitado. Verifique la conexión y los permisos";
        return JsonDtoResponse.serviceUnavailable(message).toResponseEntity();
    }

    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<JsonDtoResponse<Object>> timeoutException(TimeoutException ex, HttpServletRequest request) {
        log.error("TIEMPO DE ESPERA EXCEDIDO ===>", ex);
        message = "Tiempo de espera excedido. Por favor, inténtelo de nuevo más tarde.";
        return JsonDtoResponse.gatewayTimeout(message).toResponseEntity();
    }

    @ExceptionHandler(RejectedExecutionException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleRejectedExecutionException(RejectedExecutionException ex) {
        log.error("EJECUCIÓN RECHAZADA ===>", ex);
        message = "La operación fue rechazada. El sistema está ocupado o no puede procesar la solicitud en este momento.";
        return JsonDtoResponse.serviceUnavailable(message).toResponseEntity();
    }

    @ExceptionHandler(CompletionException.class)
    public ResponseEntity<JsonDtoResponse<Object>> completionException(CompletionException ex, HttpServletRequest request) {
        log.error("EXCEPCIÓN DE COMPLETADO ===>", ex);
        message = "Error al completar la operación. Por favor, inténtelo de nuevo más tarde.";
        return JsonDtoResponse.error(message, HttpStatus.INTERNAL_SERVER_ERROR.value(), null).toResponseEntity();
    }

    @ExceptionHandler(ExecutionException.class)
    public ResponseEntity<JsonDtoResponse<Object>> executionException(ExecutionException ex, HttpServletRequest request) {
        log.warn("EJECUCIÓN FALLIDA ===>", ex);
        message = "Error al ejecutar la operación. Por favor, inténtelo de nuevo más tarde.";
        return JsonDtoResponse.error(message, HttpStatus.INTERNAL_SERVER_ERROR.value(), null).toResponseEntity();
    }

    // ---------------------- ERRRORES DE BASES DE DATOS ----------------------

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<JsonDtoResponse<Object>> sQLException(SQLException ex, HttpServletRequest request) {
        log.warn("ERROR DE BASE DE DATOS ===>", ex);
        stateCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        message = "Error al conectar con la base de datos. Por favor, inténtelo de nuevo más tarde.";
        return JsonDtoResponse.error(message, stateCode).toResponseEntity();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.warn("VIOLACIÓN DE INTEGRIDAD DE DATOS ===>", ex);
        String rootMessage = Optional.of(ex.getMostSpecificCause()).map(Throwable::getMessage).orElse("")
                .toLowerCase();
        String userMessage;
        if (rootMessage.contains("unique") ||  rootMessage.contains("duplicate")) {
            userMessage = "Registro duplicado en llave única";
        } else if (rootMessage.contains("foreign key") || rootMessage.contains("violates foreign key")) {
            userMessage = "No se puede realizar la operación porque hay una relación con otra entidad";
        } else if (rootMessage.contains("null") || rootMessage.contains("not-null")) {
            userMessage = "Hay campos obligatorios que no fueron enviados o están vacíos";
        } else {
            userMessage = "Error de integridad de datos. Verifique los datos enviados";
        }
        return JsonDtoResponse.conflict(userMessage).toResponseEntity();
    }

    @ExceptionHandler(UnexpectedRollbackException.class)
    public ResponseEntity<JsonDtoResponse<Object>> unexpectedRollbackException(UnexpectedRollbackException ex, HttpServletRequest request) {
        log.warn("TRANSACCIÓN DESHECHA ===>", ex);
        stateCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        message = "La transacción fue deshecha. Por favor, inténtelo de nuevo.";
        return JsonDtoResponse.error(message, stateCode).toResponseEntity();
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleTransactionSystemException(TransactionSystemException ex) {
        log.error("ERROR DE TRANSACCIÓN ===>", ex);
        message = "Error de validación o persistencia durante la transacción. Verifique los datos enviados.";
        return JsonDtoResponse.error(message, HttpStatus.BAD_REQUEST.value(), null).toResponseEntity();
    }



    // ---------------------- ERRORES DE AUTENTICACION - SEGURIDADD  ----------------------
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        log.error("AUTORIZACIÓN DENEGADA ===>", ex);
        message = "No tiene permiso para acceder a este recurso";
        return JsonDtoResponse.forbidden(message).toResponseEntity();
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleAuthenticationCredentialsNotFound(AuthenticationCredentialsNotFoundException ex) {
        log.warn("CREDENCIALES NO ENCONTRADAS ===>", ex);
        message = "Credenciales de autenticación no encontradas. Por favor, inicie sesión nuevamente";
        return JsonDtoResponse.unauthorized(message).toResponseEntity();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleAuthenticationException(AuthenticationException ex) {
        log.warn("AUTENTICACIÓN FALLIDA ===>", ex);
        message = "Autenticación fallida. Verifique sus credenciales";
        return JsonDtoResponse.unauthorized(message).toResponseEntity();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("ACCESO DENEGADO ===>", ex);
        message = "Acceso denegado. No tiene permisos para realizar esta operación";
        return JsonDtoResponse.forbidden(message).toResponseEntity();
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleJwtException(JwtException ex) {
        log.warn("TOKEN JWT INVÁLIDO ===>", ex);
        message = "Token JWT inválido o malformado. Por favor, inicie sesión nuevamente";
        return JsonDtoResponse.unauthorized(message).toResponseEntity();
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleBadCredentials(BadCredentialsException ex) {
        log.warn("CREDENCIALES INVÁLIDAS ===>", ex);
        message = "Usuario o contraseña incorrectos";
        return JsonDtoResponse.unauthorized(message).toResponseEntity();
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleUsernameNotFound(UsernameNotFoundException ex) {
        log.warn("USUARIO NO ENCONTRADO ===>", ex);
        message = "Usuario no encontrado";
        return JsonDtoResponse.notFound(message).toResponseEntity();
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleInsufficientAuth(InsufficientAuthenticationException ex) {
        log.warn("AUTENTICACIÓN INSUFICIENTE ===>", ex);
        message = "Debe autenticarse antes de acceder a este recurso";
        return JsonDtoResponse.unauthorized(message).toResponseEntity();
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleExpiredCredentials(CredentialsExpiredException ex) {
        log.warn("CREDENCIALES EXPIRADAS ===>", ex);
        message = "Sus credenciales han expirado. Por favor, actualícelas";
        return JsonDtoResponse.unauthorized(message).toResponseEntity();
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleLocked(AccountLockedException ex) {
        log.warn("CUENTA BLOQUEADA ===>", ex);
        message = "Su cuenta está bloqueada. Por favor, contacte al administrador";
        return JsonDtoResponse.unauthorized(message).toResponseEntity();
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleDisabled(DisabledException ex) {
        log.warn("CUENTA DESACTIVADA ===>", ex);
        message = "Su cuenta está desactivada. Por favor, contacte al administrador";
        return JsonDtoResponse.unauthorized(message).toResponseEntity();
    }

    // -------------------------- ERRORES DE VALIDACION --------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<JsonDtoResponse<List<ValidationErrorDto>>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.warn("VALIDACIÓN DE ARGUMENTOS FALLIDA ===>", ex);
        List<ValidationErrorDto> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ValidationErrorDto(
                        fieldError.getField(),
                        fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "Mensaje no proporcionado",
                        fieldError.getRejectedValue() != null ? fieldError.getRejectedValue() : "Valor no proporcionado"
                ))
                .toList();

        return JsonDtoResponse.badRequest("Validación fallida", errors).toResponseEntity();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<JsonDtoResponse<List<ValidationErrorDto>>> handleConstraintViolationException(ConstraintViolationException ex) {
        log.warn("VALIDACIÓN DE PARÁMETROS FALLIDA ===>", ex);

        List<ValidationErrorDto> errors = ex.getConstraintViolations().stream()
                .map(violation -> {
                    String field = violation.getPropertyPath().toString();
                    message = violation.getMessage() != null ? violation.getMessage() : "Mensaje no proporcionado";
                    Object rejectedValue = violation.getInvalidValue() != null ? violation.getInvalidValue() : "Valor no proporcionado";
                    return new ValidationErrorDto(field, message, rejectedValue);
                })
                .toList();
        message = "Error de validación en parámetros";
        return JsonDtoResponse.badRequest(message, errors).toResponseEntity();
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleMissingRequestHeader(MissingRequestHeaderException ex) {
        log.warn("HEADER FALTANTE ===>", ex);
        stateCode = HttpStatus.BAD_REQUEST.value();
        message = "Header %s requerido en la solicitud".formatted(ex.getHeaderName());
        return JsonDtoResponse.error(message, stateCode).toResponseEntity();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleMissingRequestParam(MissingServletRequestParameterException ex) {
        log.warn("PARÁMETRO FALTANTE ===>", ex);
        stateCode = HttpStatus.BAD_REQUEST.value();
        message = "Parámetro %s requerido en la solicitud".formatted(ex.getParameterName());
        return JsonDtoResponse.error(message, stateCode).toResponseEntity();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn("TIPO DE DATO NO COINCIDE ===>", ex);
        message = "Parámetro inválido o malformado";
        return JsonDtoResponse.badRequest(message, null).toResponseEntity();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("CUERPO DE SOLICITUD INVÁLIDO ===>", ex);
        stateCode = HttpStatus.BAD_REQUEST.value();
        message = "Cuerpo de la solicitud inválido o malformado. Verifique el JSON enviado";
        return JsonDtoResponse.error(message, stateCode).toResponseEntity();
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleBindException(BindException ex) {
        log.warn("ERROR DE BINDING ===>", ex);
        message = "Error al mapear los parámetros del formulario. Verifique los campos enviados";
        return JsonDtoResponse.badRequest(message, null).toResponseEntity();
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("ARGUMENTO INVÁLIDO ===>", ex);
        message = "Argumento inválido ";
        return JsonDtoResponse.badRequest(message, null).toResponseEntity();
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleMissingPathVariable(MissingPathVariableException ex) {
        log.warn("VARIABLE DE RUTA FALTANTE ===>", ex);
        stateCode = HttpStatus.BAD_REQUEST.value();
        message = "Parámetro %s requerido en la ruta".formatted(ex.getVariableName());
        return JsonDtoResponse.error(message, stateCode).toResponseEntity();
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleConversionFailedException(ConversionFailedException ex) {
        log.warn("ERROR DE CONVERSIÓN DE TIPO ===>", ex);
        message = "Error al convertir el tipo de dato. Verifique los parámetros enviados";
        return JsonDtoResponse.badRequest(message, null).toResponseEntity();
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleHttpMessageConversionException(HttpMessageConversionException ex) {
        log.warn("ERROR DE CONVERSIÓN DE MENSAJE HTTP ===>", ex);
        message = "Error al convertir el mensaje HTTP. Verifique el formato y los datos enviados";
        return JsonDtoResponse.badRequest(message, null).toResponseEntity();
    }

    // ----------------------- ERRORES HTTP, METODO, TIPO DE CONTENDIO -----------------------

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        log.warn("MÉTODO NO SOPORTADO ===>", ex);
        stateCode = HttpStatus.METHOD_NOT_ALLOWED.value();
        String method = ex.getMethod(); // solo el metodo HTTP, no la URL completa
        message = "El método HTTP '%s' no está permitido para esta ruta".formatted(method);
        return JsonDtoResponse.error(message, stateCode).toResponseEntity();
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleHttpMediaTypeNotSupportedException
            (HttpMediaTypeNotSupportedException ex) {
        log.warn("TIPO DE CONTENIDO NO SOPORTADO ===>", ex);
        stateCode = HttpStatus.UNSUPPORTED_MEDIA_TYPE.value();
        message = "Tipo de contenido no soportado. Verifique el tipo de contenido de la solicitud";
        return JsonDtoResponse.error(message, stateCode).toResponseEntity();
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    public ResponseEntity<JsonDtoResponse<Object>> handleNoHandlerFoundException(HttpServletRequest request, HttpServletResponse response, NoHandlerFoundException ex) {
        log.warn("RUTA NO EXISTENTE ===>", ex);
        stateCode = HttpStatus.NOT_FOUND.value();
        message = "La ruta '%s' no existe o no está implementada".formatted(request.getRequestURI());
        return JsonDtoResponse.error(message, stateCode).toResponseEntity();
    }
    

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleNoResourceFound(NoResourceFoundException ex) {
        log.warn("RECURSO NO ENCONTRADO ===>", ex);
        stateCode = HttpStatus.NOT_FOUND.value();
        String mensaje = "Recurso no encontrado";
        return JsonDtoResponse.error(mensaje, stateCode).toResponseEntity();
    }


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        log.warn("TAMAÑO DE ARCHIVO EXCEDIDO ===>", ex);
        message = "El archivo excede el tamaño máximo permitido de %s bytes".formatted(ex.getMaxUploadSize());
        return JsonDtoResponse.badRequest(message, null)
                .toResponseEntity();
    }


    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleFileNotFoundException(FileNotFoundException ex) {
        log.warn("ARCHIVO NO ENCONTRADO ===>", ex);
        message = "El archivo solicitado no fue encontrado en el servidor";
        return JsonDtoResponse.notFound(message).toResponseEntity();
    }


    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleHttpMediaTypeNotAcceptableException
            (HttpMediaTypeNotAcceptableException ex) {
        log.warn("FORMATO NO ACEPTABLE ===>", ex);
        stateCode = HttpStatus.NOT_ACCEPTABLE.value();
        message = "Formato de respuesta no aceptable. Verifique los encabezados de la solicitud";
        return JsonDtoResponse.error(message, stateCode).toResponseEntity();
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleJsonProcessingException(JsonProcessingException ex) {
        log.warn("ERROR DE PROCESAMIENTO JSON ===>", ex);
        message = "Error al procesar el JSON enviado. Verifique el formato y los datos";
        return JsonDtoResponse.badRequest(message, null)
                .toResponseEntity();
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleHttpClientErrorException(HttpClientErrorException ex) {
        log.warn("ERROR DE CLIENTE HTTP ===>", ex);
        int status = ex.getStatusCode().value();
        message = String.format("Error HTTP %d: %s", status, ex.getStatusText());
        return JsonDtoResponse.error(message, status, null).toResponseEntity();
    }


    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleHttpServerErrorException(HttpServerErrorException ex) {
        log.warn("ERROR DEL SERVIDOR REMOTO ===>", ex);
        int status = ex.getStatusCode().value();
        message = String.format("Error remoto (código %d): %s", status, ex.getStatusText());
        return JsonDtoResponse.error(message, status, null).toResponseEntity();
    }


    @ExceptionHandler(FileAlreadyExistsException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleFileAlreadyExistsException(FileAlreadyExistsException ex) {
        log.warn("ARCHIVO YA EXISTE ===>", ex);
        message = "El archivo ya existe en la ubicación especificada";
        return JsonDtoResponse.conflict(message).toResponseEntity();
    }


    // ---------------------- ERRORES GENERALES / INESPERADOR ----------------------
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleNullPointerException(NullPointerException ex) {
        log.warn("EXCEPCIÓN DE PUNTERO NULO ===>", ex);
        stateCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        message = "Se produjo un error inesperado. Por favor, inténtelo de nuevo más tarde";
        return JsonDtoResponse.error(message, stateCode).toResponseEntity();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleIllegalStateException(IllegalStateException ex) {
        log.error("ESTADO INVÁLIDO ===>", ex);
        stateCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        message = "El sistema no se encuentra en un estado válido para esta operación. Por favor, inténtelo de nuevo más tarde.";
        return JsonDtoResponse.error(message, stateCode, null).toResponseEntity();
    }

    @ExceptionHandler(NoSuchMethodException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleNoSuchMethodException(NoSuchMethodException ex) {
        log.error("MÉTODO NO ENCONTRADO ===>", ex);
        stateCode = HttpStatus.NOT_FOUND.value();
        message = "El método solicitado no existe o no está implementado";
        return JsonDtoResponse.notFound(message).toResponseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleGeneralException(Exception ex) {
        log.error("EXCEPCIÓN GENERAL ===>", ex);
        stateCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        message = "Se produjo un error inesperado. Por favor, inténtelo de nuevo más tarde";
        return JsonDtoResponse.error(message, stateCode).toResponseEntity();
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<JsonDtoResponse<Object>> unsupportedOperationException(UnsupportedOperationException
                                                                                         ex, HttpServletRequest request) {
        log.warn("OPERACIÓN NO SOPORTADA ===>", ex);
        message = "Operación no soportada. Por favor, contacte al administrador del sistema";
        return JsonDtoResponse.notImplemented(message).toResponseEntity();
    }

    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity<JsonDtoResponse<Object>> handleInterruptedException(InterruptedException ex) {
        log.error("HILO INTERRUMPIDO ===>", ex);
        message = "Un problema interno";
        return JsonDtoResponse.error(message, HttpStatus.INTERNAL_SERVER_ERROR.value(), null).toResponseEntity();
    }
}
