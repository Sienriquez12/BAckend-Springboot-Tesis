package com.especlub.match.shared.validations.validators;

import com.especlub.match.shared.validations.annotations.CustomOnlyLetters;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Validates that a string contains only letters (uppercase, lowercase, with accents/diacritics) and spaces.
 * - Supports Unicode letters for LATAM (á, é, í, ó, ú, ü, ñ, etc.).
 * - Does not allow digits or symbols.
 * - Ignores null or blank values (delegated to @NotBlank if required).
 */
public class CustomOnlyLettersValidator implements ConstraintValidator<CustomOnlyLetters, String> {

private static final Pattern LETTERS_PATTERN =  Pattern.compile("^(?=.*\\p{L})[\\p{L}\\p{M} ]+$");
    @Override
    public void initialize(CustomOnlyLetters constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return LETTERS_PATTERN.matcher(value).matches();
    }
}
