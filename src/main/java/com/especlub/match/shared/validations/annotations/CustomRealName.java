package com.especlub.match.shared.validations.annotations;

import com.especlub.match.shared.validations.validators.CustomRealNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomRealNameValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomRealName {

    String message() default "El nombre solo puede contener letras, espacios, guiones o apóstrofes";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
