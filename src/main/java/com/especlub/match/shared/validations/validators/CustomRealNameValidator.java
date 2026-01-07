package com.especlub.match.shared.validations.validators;

import com.especlub.match.shared.validations.annotations.CustomRealName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Validates real names:
 * - Allows Unicode letters (all), spaces, apostrophes ( ' and ’ ), and hyphens (-).
 * - Does not allow digits or other symbols.
 * - Requires at least one letter.
 * - Validates min/max length after trim().
 */
public class CustomRealNameValidator implements ConstraintValidator<CustomRealName, String> {

    private static final Pattern REAL_NAME_PATTERN =  Pattern.compile("^(?=.*\\p{L})[\\p{L}\\p{M} .'\\-’]+$");

    @Override
    public void initialize(CustomRealName constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return REAL_NAME_PATTERN.matcher(value.trim()).matches();
    }

}
