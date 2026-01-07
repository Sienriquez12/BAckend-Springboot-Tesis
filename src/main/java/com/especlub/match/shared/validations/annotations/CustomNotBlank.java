package com.especlub.match.shared.validations.annotations;

import com.especlub.match.shared.validations.validators.CustomNotBlankValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomNotBlankValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomNotBlank {
    String message() default "El campo no debe contener solo espacios en blanco";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}