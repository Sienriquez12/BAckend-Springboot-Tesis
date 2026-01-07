package com.especlub.match.shared.validations.validators;

import com.especlub.match.shared.validations.annotations.CustomNotBlank;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomNotBlankValidatorTest {
    private Validator validator;

    private static class TestClass {
        @CustomNotBlank
        String value;
        TestClass(String value) { this.value = value; }
    }

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void allowsNullValue() {
        TestClass obj = new TestClass(null);
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), "No debe haber violaciones para valor nulo");
    }

    @Test
    void rejectsEmptyString() {
        TestClass obj = new TestClass("");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty(), "Debe haber violaciones para cadena vacía");
    }

    @Test
    void rejectsOnlySpaces() {
        TestClass obj = new TestClass("   ");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty(), "Debe haber violaciones para solo espacios");
    }

    @Test
    void allowsTextWithSpaces() {
        TestClass obj = new TestClass(" texto ");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), "No debe haber violaciones para texto válido con espacios");
    }

    @Test
    void allowsNormalText() {
        TestClass obj = new TestClass(" texto");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), "No debe haber violaciones para texto válido");
    }
}

