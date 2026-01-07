package com.especlub.match.dto.request;

import com.especlub.match.shared.validations.annotations.CustomEcuadorCedula;
import com.especlub.match.shared.validations.annotations.CustomOnlyDigits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PinValidationRequestDto {
    @NotBlank(message = "La cédula no puede estar vacía")
    @CustomEcuadorCedula
    private String nationalId;

    @NotBlank(message = "El correo electrónico no puede estar vacío")
    @Email(message = "El correo electrónico no es válido")
    private String email;

    @NotBlank(message = "El PIN no puede estar vacío")
    @CustomOnlyDigits
    private String pin;
}
