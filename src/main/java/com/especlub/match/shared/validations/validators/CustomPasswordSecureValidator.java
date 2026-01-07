package com.especlub.match.shared.validations.validators;

import com.especlub.match.shared.validations.annotations.CustomPasswordSecure;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;
/*
    * Validates that a password is secure:
    * - At least 8 characters long
    * - Contains at least one uppercase letter
    * - Contains at least one lowercase letter
    * - Contains at least one digit
    * - Contains at least one special character
 */
public class CustomPasswordSecureValidator implements ConstraintValidator<CustomPasswordSecure, String> {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
    "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$"
    );

    @Override
    public void initialize(CustomPasswordSecure constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return PASSWORD_PATTERN.matcher(value).matches();
    }
}

