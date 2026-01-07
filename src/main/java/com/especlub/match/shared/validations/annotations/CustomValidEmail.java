package com.especlub.match.shared.validations.annotations;

import com.especlub.match.shared.validations.validators.CustomValidEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = { CustomValidEmailValidator.class })
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomValidEmail {

    String message() default "El email debe ser válido basado en nombre@dominio.extension";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
