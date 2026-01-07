package com.especlub.match.dto.request;

import com.especlub.match.shared.validations.annotations.CustomPasswordSecure;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordUpdateInternalRequestDto {
    @NotBlank(message = "La contraseña actual no puede estar vacía")
    private String currentPassword;

    @NotBlank(message = "La nueva contraseña no puede estar vacía")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    @CustomPasswordSecure
    private String newPassword;
}

