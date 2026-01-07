package com.especlub.match.shared.validations.validators;

import com.especlub.match.shared.validations.annotations.CustomOnlyDigits;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Validates that a string contains only digits (0–9).
 * - Allows any length.
 * - Ignores null or blank values (delegated to @NotBlank if required).
 */
public class CustomOnlyDigitsValidator implements ConstraintValidator<CustomOnlyDigits, String> {

    private static final Pattern DIGITS_PATTERN = Pattern.compile("^\\d+$");

    @Override
    public void initialize(CustomOnlyDigits constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return DIGITS_PATTERN.matcher(value).matches();
    }
}
