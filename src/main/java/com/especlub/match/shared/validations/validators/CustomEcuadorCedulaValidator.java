package com.especlub.match.shared.validations.validators;

import com.especlub.match.shared.validations.annotations.CustomEcuadorCedula;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Validates Ecuadorian national ID ("cédula") for natural persons:
 * - Exactly 10 digits.
 * - Province code between 01 and 24.
 * - Third digit < 6 (natural person).
 * - Last digit is a valid checksum (mod 10) with coefficients [2,1,2,1,2,1,2,1,2].
 * - Null/blank is considered valid (use @NotBlank to enforce required).
 */
public class CustomEcuadorCedulaValidator implements ConstraintValidator<CustomEcuadorCedula, String> {

    private static final Pattern TEN_DIGITS = Pattern.compile("^\\d{10}$");

    @Override
    public void initialize(CustomEcuadorCedula constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        final String id = value.trim();

        if (!TEN_DIGITS.matcher(id).matches()) {
            return false;
        }

        int province = Integer.parseInt(id.substring(0, 2));
        if (province < 1 || province > 24) {
            return false;
        }

        int third = Character.digit(id.charAt(2), 10);
        if (third < 0 || third > 5) {
            return false;
        }

        int[] coef = {2,1,2,1,2,1,2,1,2};
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            int digit = Character.digit(id.charAt(i), 10);
            int prod = digit * coef[i];
            if (prod >= 10) prod -= 9;
            sum += prod;
        }
        int checkCalculated = (10 - (sum % 10)) % 10;
        int checkGiven = Character.digit(id.charAt(9), 10);

        return checkCalculated == checkGiven;
    }
}
