package com.especlub.match.dto.request;

import com.especlub.match.shared.validations.annotations.CustomEcuadorCedula;
import com.especlub.match.shared.validations.annotations.CustomOnlyDigits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordRecoveryRequestDto {
    @NotBlank(message = "La cédula no puede estar vacía")
    @CustomEcuadorCedula
    @CustomOnlyDigits
    private String nationalId;

    @NotBlank(message = "El correo electrónico no puede estar vacío")
    @Email(message = "El correo electrónico no es válido")
    @Size(max = 100, message = "El correo electrónico no puede tener más de 100 caracteres")
    private String email;
}

