package com.especlub.match.shared.validations.annotations;

import com.especlub.match.shared.validations.validators.CustomOnlyDigitsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomOnlyDigitsValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)

public @interface CustomOnlyDigits {
    String message() default "El valor solo puede contener dígitos del 0 al 9";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
