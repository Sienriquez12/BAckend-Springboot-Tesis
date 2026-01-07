package com.especlub.match.dto.request;

import com.especlub.match.shared.validations.annotations.CustomOnlyDigits;
import com.especlub.match.shared.validations.annotations.CustomValidEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ActivationRequestDto {
    @NotBlank(message = "El correo electrónico es obligatorio")
    @CustomValidEmail
    @Pattern(
            regexp = ".*@espe\\.edu\\.ec$",
            message = "El correo debe pertenecer al dominio @espe.edu.ec"
    )
    private String email;

    @NotBlank(message = "El PIN es obligatorio")
    @CustomOnlyDigits
    @Size(min = 6, max = 6, message = "El PIN debe tener exactamente 6 dígitos")
    private String pin;
}

