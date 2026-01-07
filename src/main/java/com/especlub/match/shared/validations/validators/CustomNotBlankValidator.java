package com.especlub.match.shared.validations.validators;

import com.especlub.match.shared.validations.annotations.CustomNotBlank;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * Validator for the @CustomNotBlank annotation.
 * This validator checks if a string is either null or contains at least one non-space character.
 */
public class CustomNotBlankValidator implements ConstraintValidator<CustomNotBlank, String> {
    private static final Pattern NOT_ONLY_SPACES = Pattern.compile(".*\\S.*");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || NOT_ONLY_SPACES.matcher(value).matches();
    }
}