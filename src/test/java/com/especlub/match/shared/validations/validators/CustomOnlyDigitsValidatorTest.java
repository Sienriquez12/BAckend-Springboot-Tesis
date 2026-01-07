package com.especlub.match.shared.validations.validators;

import com.especlub.match.shared.validations.annotations.CustomOnlyDigits;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomOnlyDigitsValidatorTest {

    private static Validator validator;

    static class TestClass {
        @CustomOnlyDigits
        String value;

        TestClass(String value) {
            this.value = value;
        }
    }

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validOnlyDigitsTest() {
        TestClass obj = new TestClass("1234567890");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), "No debe haber violaciones para solo dígitos");
    }

    @Test
    void invalidWithLettersTest() {
        TestClass obj = new TestClass("1234abc567");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty(), "Debe haber violaciones para valores con letras");
        assertEquals("El valor solo puede contener dígitos del 0 al 9", violations.iterator().next().getMessage());
    }

    @Test
    void invalidWithSpecialCharactersTest() {
        TestClass obj = new TestClass("1234-5678");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty(), "Debe haber violaciones para valores con caracteres especiales");
        assertEquals("El valor solo puede contener dígitos del 0 al 9", violations.iterator().next().getMessage());
    }

    @Test
    void validEmptyStringTest() {
        TestClass obj = new TestClass("");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), "No debe haber violaciones para cadena vacía (si así lo define la lógica)");
    }
}
