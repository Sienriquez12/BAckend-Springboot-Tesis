package com.especlub.match.shared.validations.validators;

import com.especlub.match.shared.validations.annotations.CustomAlfanumeric;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class CustomAlfanumericValidator implements ConstraintValidator<CustomAlfanumeric, String> {
    private static final Pattern ALFANUMERIC_PATTERN = Pattern.compile("^[\\p{L}\\d ]+$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        return ALFANUMERIC_PATTERN.matcher(value).matches();
    }
}

