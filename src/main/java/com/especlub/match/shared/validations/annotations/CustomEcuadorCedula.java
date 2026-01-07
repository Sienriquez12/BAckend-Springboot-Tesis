package com.especlub.match.shared.validations.annotations;
import com.especlub.match.shared.validations.validators.CustomEcuadorCedulaValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomEcuadorCedulaValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomEcuadorCedula {

    String message() default "Cédula de identidad ecuatoriana inválida";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
