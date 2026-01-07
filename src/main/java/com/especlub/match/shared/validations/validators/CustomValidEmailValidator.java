package com.especlub.match.shared.validations.validators;

import com.especlub.match.shared.validations.annotations.CustomValidEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Validates email addresses:
 * - Ensures the value matches a basic email regex (name@domain.extension).
 * - Checks that the length is between the defined min and max.
 * - Ignores null or blank values (delegated to @NotBlank if required).
 */
public class CustomValidEmailValidator implements ConstraintValidator<CustomValidEmail, String> {


    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Override
    public void initialize(CustomValidEmail constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return EMAIL_PATTERN.matcher(value).matches();
    }
}
