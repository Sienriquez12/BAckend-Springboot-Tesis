package com.especlub.match.shared.validations.annotations;

import com.especlub.match.shared.validations.validators.CustomPassportNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = CustomPassportNumberValidator.class)
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
public @interface CustomPassportNumber {
    String message() default "El número de pasaporte no es válido. Debe ser alfanumérico y tener entre 6 y 9 caracteres.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

