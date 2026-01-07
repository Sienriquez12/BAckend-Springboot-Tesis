package com.especlub.match.shared.validations.validators;

import java.time.LocalDate;
import java.time.Period;

import com.especlub.match.shared.validations.annotations.CustomBirthDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
/**
 * Validator for checking if a given LocalDate represents a birth date
 * that makes the person between 18 and 100 years old.
 */
public class CustomBirthDateValidator implements ConstraintValidator<CustomBirthDate, LocalDate> {
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        LocalDate today = LocalDate.now();
        int age = Period.between(value, today).getYears();
        return age >= 18 && age <= 100;
    }
}

