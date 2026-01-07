package com.especlub.match.shared.validations.validators;

import com.especlub.match.shared.validations.annotations.CustomEcuadorCedula;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomEcuadorCedulaTest {

    private static Validator validator;

    static class TestClass {
        @CustomEcuadorCedula
        String cedula;

        TestClass(String cedula) {
            this.cedula = cedula;
        }
    }

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validEcuadorianIdTest() {
        TestClass obj = new TestClass("2300287246");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty());
    }

    @Test
    void invalidEcuadorianIdTest() {
        TestClass obj = new TestClass("1234567890");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty());
        assertEquals("Cédula de identidad ecuatoriana inválida", violations.iterator().next().getMessage());
    }
}