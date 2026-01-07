package com.especlub.match.shared.validations.annotations;

import com.especlub.match.shared.validations.validators.CustomBirthDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = CustomBirthDateValidator.class)
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
public @interface CustomBirthDate {
    String message() default "La edad debe ser mayor o igual a 18 y menor o igual a 100 años";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
