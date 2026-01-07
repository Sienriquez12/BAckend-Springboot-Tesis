package com.especlub.match.shared.validations.annotations;

import com.especlub.match.shared.validations.validators.CustomPasswordSecureValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = CustomPasswordSecureValidator.class)
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
public @interface CustomPasswordSecure {
    String message() default "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

