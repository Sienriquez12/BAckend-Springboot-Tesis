package com.especlub.match.shared.validations.annotations;

import com.especlub.match.shared.validations.validators.CustomAlfanumericValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomAlfanumericValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomAlfanumeric {
    String message() default "Solo se permiten letras latinas, números y espacios";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
