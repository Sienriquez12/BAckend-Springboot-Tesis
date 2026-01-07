package com.especlub.match.shared.validations.validators;

import com.especlub.match.shared.validations.annotations.CustomUuidFormat;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for checking if a string is a valid UUID format.
 */
public class CustomUuidValidator implements ConstraintValidator<CustomUuidFormat, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        try {
            java.util.UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
