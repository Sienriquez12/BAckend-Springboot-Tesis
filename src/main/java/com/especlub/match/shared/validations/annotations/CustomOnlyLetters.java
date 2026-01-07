package com.especlub.match.shared.validations.annotations;

import com.especlub.match.shared.validations.validators.CustomOnlyLettersValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomOnlyLettersValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)

public @interface CustomOnlyLetters {

    String message() default "El valor solo puede contener letras (mayúsculas, minúsculas, con tildes) y espacios";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
