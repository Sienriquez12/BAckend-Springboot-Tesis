package com.especlub.match.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Objeto de solicitud para autenticación del usuario")
public class LoginInternalRequest {

    @Schema(description = "Nombre de usuario para autenticación", example = "admin-biometrix")
    @Size(max = 50, message = "El nombre de usuario no debe exceder los 50 caracteres")
    private String username;

    @Schema(description = "Correo electrónico del usuario para autenticación")
    @Size(max = 100, message = "El correo electrónico no debe exceder los 100 caracteres")
    private String email;

    @Schema(description = "Contraseña del usuario para autenticación", example = "P@ssw0rd*+")
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, max = 64, message = "La contraseña debe tener entre 8 y 64 caracteres")
    private String password;
}