package com.especlub.match.shared.validations.annotations;

import com.especlub.match.shared.validations.validators.CustomIpAddressValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomIpAddressValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomIpAddress {
    String message() default "La dirección IP no es válida";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

