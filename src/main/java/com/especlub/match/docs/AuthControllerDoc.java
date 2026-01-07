package com.especlub.match.docs;

import com.especlub.match.dto.request.*;
import com.especlub.match.dto.response.JsonDtoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "Autenticación y Registro", description = "Controlador para operaciones de autenticación, registro, activación de usuarios, recuperación y actualización de contraseñas. Incluye login, validación de token, cierre de sesión, registro y activación de usuarios, así como recuperación y actualización de contraseñas mediante PIN.")
public interface AuthControllerDoc {

    @Operation(
        summary = "Iniciar sesión de usuario",
        description = "Permite a un usuario iniciar sesión con su nombre de usuario o correo electrónico y contraseña. Devuelve un JWT en una cookie si el login es exitoso. Envía un correo de notificación de inicio de sesión."
    )
    @ApiResponse(responseCode = "200", description = "Inicio de sesión correcto",
        content = @Content(schema = @Schema(implementation = JsonDtoResponse.class),
            examples = @ExampleObject(value = "{\n  'result': true,\n  'message': 'Inicio de sesión correcto',\n  'statusCode': 200\n}")))
    @ApiResponse(responseCode = "401", description = "Credenciales incorrectas",
        content = @Content(schema = @Schema(implementation = JsonDtoResponse.class)))
    ResponseEntity<JsonDtoResponse<Boolean>> login(LoginInternalRequest loginInternalRequest, HttpServletRequest request, HttpServletResponse responseHttp);

    @Operation(
        summary = "Validar token JWT",
        description = "Valida que el token JWT enviado en la cookie o header sea válido y no haya expirado."
    )
    @ApiResponse(responseCode = "200", description = "Token validado",
        content = @Content(schema = @Schema(implementation = JsonDtoResponse.class)))
    @ApiResponse(responseCode = "401", description = "Token inválido o expirado",
        content = @Content(schema = @Schema(implementation = JsonDtoResponse.class)))
    ResponseEntity<JsonDtoResponse<Void>> validateToken(HttpServletRequest request);

    @Operation(
        summary = "Cerrar sesión",
        description = "Cierra la sesión del usuario eliminando la cookie del JWT."
    )
    @ApiResponse(responseCode = "200", description = "Cierre de sesión correcto",
        content = @Content(schema = @Schema(implementation = JsonDtoResponse.class)))
    ResponseEntity<JsonDtoResponse<Boolean>> logout(HttpServletRequest request, HttpServletResponse responseHttp);

    @Operation(
        summary = "Pre-registro de usuario estudiante",
        description = "Permite a un estudiante pre-registrarse. Envía un PIN de activación al correo, elimina registros previos no activados y sus PINs."
    )
    @ApiResponse(responseCode = "200", description = "Registro de usuario correcto",
        content = @Content(schema = @Schema(implementation = JsonDtoResponse.class),
            examples = @ExampleObject(value = "{\n  'result': true,\n  'message': 'Registro de usuario correcto',\n  'statusCode': 200\n}")))
    ResponseEntity<JsonDtoResponse<Boolean>> register(UserInfoRequestDto registerRequest);

    @Operation(
        summary = "Activar usuario con PIN",
        description = "Activa un usuario usando el PIN enviado al correo. Valida el PIN, activa el usuario, marca el PIN como usado y envía correo de bienvenida."
    )
    @ApiResponse(responseCode = "200", description = "Usuario activado correctamente",
        content = @Content(schema = @Schema(implementation = JsonDtoResponse.class),
            examples = @ExampleObject(value = "{\n  'result': true,\n  'message': 'Usuario activado correctamente',\n  'statusCode': 200\n}")))
    ResponseEntity<JsonDtoResponse<Boolean>> activateUser(ActivationRequestDto activationRequest);

    @Operation(
        summary = "Solicitar PIN de recuperación de contraseña",
        description = "Envía un PIN de recuperación al correo si los datos coinciden."
    )
    @ApiResponse(responseCode = "200", description = "Instrucciones de recuperación enviadas si el correo existe",
        content = @Content(schema = @Schema(implementation = JsonDtoResponse.class),
            examples = @ExampleObject(value = "{\n  'result': true,\n  'message': 'Instrucciones de recuperación enviadas si el correo existe',\n  'statusCode': 200\n}")))
    ResponseEntity<JsonDtoResponse<Boolean>> recoverPassword(PasswordRecoveryRequestDto passwordRecoveryRequestDto);

    @Operation(
        summary = "Actualizar contraseña con PIN",
        description = "Actualiza la contraseña del usuario validando el PIN enviado al correo. Marca el PIN como usado."
    )
    @ApiResponse(responseCode = "200", description = "Contraseña actualizada correctamente",
        content = @Content(schema = @Schema(implementation = JsonDtoResponse.class),
            examples = @ExampleObject(value = "{\n  'result': true,\n  'message': 'Contraseña actualizada correctamente',\n  'statusCode': 200\n}")))
    ResponseEntity<JsonDtoResponse<Boolean>> updatePasswordWithPin(PasswordUpdateWithPinRequestDto dto);
}
