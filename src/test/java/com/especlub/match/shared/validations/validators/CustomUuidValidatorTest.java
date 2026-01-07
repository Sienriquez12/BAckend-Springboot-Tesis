package com.especlub.match.shared.validations.validators;

import com.especlub.match.shared.validations.annotations.CustomUuidFormat;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomUuidValidatorTest {

    private static Validator validator;

    static class TestClass {
        @CustomUuidFormat
        String uuid;

        TestClass(String uuid) {
            this.uuid = uuid;
        }
    }

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validUuidTest() {
        TestClass obj = new TestClass("123e4567-e89b-12d3-a456-426614174000");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), "No debe haber violaciones para un UUID válido");
    }

    @Test
    void invalidUuidTest() {
        TestClass obj = new TestClass("123e4567-e89b-12d3-a456-42661417400Z"); // Letra Z no es válida
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty(), "Debe haber violaciones para un UUID inválido");
        assertEquals("El formato es inválido", violations.iterator().next().getMessage());
    }

    @Test
    void invalidUuidFormatTest() {
        TestClass obj = new TestClass("not-a-uuid");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty(), "Debe haber violaciones para un formato inválido");
        assertEquals("El formato es inválido", violations.iterator().next().getMessage());
    }
}

