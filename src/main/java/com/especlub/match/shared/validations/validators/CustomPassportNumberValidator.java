package com.especlub.match.shared.validations.validators;

import com.especlub.match.shared.validations.annotations.CustomPassportNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class CustomPassportNumberValidator implements ConstraintValidator<CustomPassportNumber, String> {

    private static final Pattern PASSPORT_PATTERN = Pattern.compile("^[A-Za-z0-9]{6,9}$");

    @Override
    public void initialize(CustomPassportNumber constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return PASSPORT_PATTERN.matcher(value).matches();
    }
}

