package com.especlub.match.shared.validations.validators;

import com.especlub.match.shared.validations.annotations.CustomNotBlank;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for the @CustomNotBlank annotation.
 * This validator checks if a string is either null or contains at least one non-space character.
 *
 * Replaced regex with a linear scan to avoid any potential backtracking/regex ReDoS issues.
 */
public class CustomNotBlankValidator implements ConstraintValidator<CustomNotBlank, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        // Scan characters once and return as soon as a non-whitespace character is found.
        for (int i = 0, len = value.length(); i < len; i++) {
            if (!Character.isWhitespace(value.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}