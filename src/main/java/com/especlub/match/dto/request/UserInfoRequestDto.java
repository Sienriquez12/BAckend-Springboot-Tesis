package com.especlub.match.dto.request;

import com.especlub.match.shared.validations.annotations.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoRequestDto {

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 3, max = 30, message = "El nombre de usuario debe tener entre 3 y 30 caracteres")
    private String username;

    @NotBlank(message = "El correo electrónico no puede estar vacío")
    @CustomValidEmail
    @Pattern(
        regexp = ".*@espe\\.edu\\.ec$",
        message = "El correo debe pertenecer al dominio @espe.edu.ec"
    )
    private String email;

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Size(min = 7, max = 50, message = "El teléfono debe tener entre 7 y 50 caracteres")
    @CustomOnlyDigits
    private String phone;

    @NotBlank(message = "El/los nombre(s) no puede(n) estar vacío(s)")
    @CustomRealName
    private String names;

    @NotBlank(message = "El/los apellido(s) no puede(n) estar vacío(s)")
    @CustomRealName
    private String surnames;

    @CustomBirthDate
    private LocalDate birthDate;

    @AssertTrue(message = "Debe aceptar los términos y condiciones")
    private Boolean acceptTerms;

    @AssertTrue(message = "Debe aceptar la política de privacidad")
    private Boolean acceptPrivacy;

    @NotBlank(message = "La cédula no puede estar vacía")
    @Size(min = 10, max = 10, message = "La cédula debe tener 10 dígitos")
    @CustomOnlyDigits(message = "La cédula solo debe contener números")
    @CustomEcuadorCedula(message = "La cédula no es válida para Ecuador")
    private String nationalId;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @CustomPasswordSecure(message = "La contraseña no cumple con los requisitos de seguridad")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    private String password;

}
