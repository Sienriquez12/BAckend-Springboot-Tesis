package com.especlub.match.shared.validations.validators;

import com.especlub.match.shared.validations.annotations.CustomAlfanumeric;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomAlfanumericValidatorTest {
    private static Validator validator;

    private static class TestClass {
        @CustomAlfanumeric
        private String value;
        TestClass(String value) { this.value = value; }
    }

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validAlfanumericTest() {
        TestClass obj = new TestClass("Juan Pérez 123");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), "No debe haber violaciones para texto alfanumérico válido");
    }

    @Test
    void validAlfanumericWithAccentsTest() {
        TestClass obj = new TestClass("Álvaro Niño 456");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), "No debe haber violaciones para texto con acentos y ñ");
    }

    @Test
    void invalidAlfanumericWithSpecialCharsTest() {
        TestClass obj = new TestClass("Juan@Pérez!");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty(), "Debe haber violaciones para caracteres especiales");
        assertEquals("Solo se permiten letras latinas, números y espacios", violations.iterator().next().getMessage());
    }

    @Test
    void emptyStringIsValidTest() {
        TestClass obj = new TestClass("");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), "No debe haber violaciones para cadena vacía");
    }

    @Test
    void nullValueIsValidTest() {
        TestClass obj = new TestClass(null);
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), "No debe haber violaciones para valor nulo");
    }
}

