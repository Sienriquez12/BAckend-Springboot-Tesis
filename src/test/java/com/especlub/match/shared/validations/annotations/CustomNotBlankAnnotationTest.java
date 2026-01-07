package com.especlub.match.shared.validations.annotations;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomNotBlankAnnotationTest {
    private Validator validator;

    private static class TestClass {
        @CustomNotBlank(message = "Campo obligatorio, no puede ser solo espacios")
        String value;
        TestClass(String value) { this.value = value; }
    }

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void customMessageIsUsed() {
        TestClass obj = new TestClass("   ");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty(), "Debe haber violaciones para solo espacios");
        assertEquals("Campo obligatorio, no puede ser solo espacios", violations.iterator().next().getMessage());
    }

    @Test
    void customMessageIsUsedForEmptyString() {
        TestClass obj = new TestClass("");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty(), "Debe haber violaciones para cadena vacía");
        assertEquals("Campo obligatorio, no puede ser solo espacios", violations.iterator().next().getMessage());
    }

    @Test
    void allowsValidText() {
        TestClass obj = new TestClass("Texto válido");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), "No debe haber violaciones para texto válido");
    }
}

