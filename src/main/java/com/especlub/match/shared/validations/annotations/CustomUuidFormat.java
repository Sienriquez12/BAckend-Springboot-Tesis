package com.especlub.match.shared.validations.annotations;
import com.especlub.match.shared.validations.validators.CustomUuidValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomUuidValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomUuidFormat {
    String message() default "El formato es inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
