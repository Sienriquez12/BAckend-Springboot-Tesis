package com.especlub.match.shared.validations.validators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CustomBirthDateValidatorTest {
    private CustomBirthDateValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new CustomBirthDateValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void testNullDateIsValid() {
        assertTrue(validator.isValid(null, context));
    }

    @Test
    void testExactly18YearsOldIsValid() {
        LocalDate date = LocalDate.now().minusYears(18);
        assertTrue(validator.isValid(date, context));
    }

    @Test
    void testExactly100YearsOldIsValid() {
        LocalDate date = LocalDate.now().minusYears(100);
        assertTrue(validator.isValid(date, context));
    }

    @Test
    void testYoungerThan18IsInvalid() {
        LocalDate date = LocalDate.now().minusYears(17).plusDays(1);
        assertFalse(validator.isValid(date, context));
    }

    @Test
    void testOlderThan100IsInvalid() {
        LocalDate date = LocalDate.now().minusYears(101);
        assertFalse(validator.isValid(date, context));
    }
}

