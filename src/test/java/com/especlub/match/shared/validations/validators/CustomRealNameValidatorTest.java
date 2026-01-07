package com.especlub.match.shared.validations.validators;

import com.especlub.match.shared.validations.annotations.CustomRealName;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomRealNameValidatorTest {

    private static Validator validator;

    static class TestClass {
        @CustomRealName
        String name;

        TestClass(String name) {
            this.name = name;
        }
    }

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validNameTest() {
        TestClass obj = new TestClass("Juan Pérez");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), "No debe haber violaciones para un nombre válido");
    }

    @Test
    void validNameWithHyphenTest() {
        TestClass obj = new TestClass("María-José");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), "No debe haber violaciones para nombre con guion");
    }

    @Test
    void validNameWithApostropheTest() {
        TestClass obj = new TestClass("O'Connor");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), "No debe haber violaciones para nombre con apóstrofe");
    }

    @Test
    void invalidNameWithNumbersTest() {
        TestClass obj = new TestClass("Juan123");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty(), "Debe haber violaciones para nombre con números");
        assertEquals("El nombre solo puede contener letras, espacios, guiones o apóstrofes", violations.iterator().next().getMessage());
    }

    @Test
    void invalidNameWithSpecialCharactersTest() {
        TestClass obj = new TestClass("Juan@Pérez");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty(), "Debe haber violaciones para nombre con caracteres especiales");
        assertEquals("El nombre solo puede contener letras, espacios, guiones o apóstrofes", violations.iterator().next().getMessage());
    }

    @Test
    void validEmptyNameTest() {
        TestClass obj = new TestClass("");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), "No debe haber violaciones para cadena vacía (si así lo define la lógica)");
    }
}
