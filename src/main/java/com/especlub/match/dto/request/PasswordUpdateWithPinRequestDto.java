package com.especlub.match.dto.request;

import com.especlub.match.shared.validations.annotations.CustomEcuadorCedula;
import com.especlub.match.shared.validations.annotations.CustomPasswordSecure;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordUpdateWithPinRequestDto {
    @NotBlank(message = "La cédula no puede estar vacía")
    @CustomEcuadorCedula
    private String nationalId;

    @NotBlank(message = "El correo electrónico no puede estar vacío")
    @Email(message = "El correo electrónico no es válido")
    private String email;

    @NotBlank(message = "El PIN no puede estar vacío")
    private String pin;

    @NotBlank(message = "La nueva contraseña no puede estar vacía")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    @CustomPasswordSecure
    private String newPassword;
}

