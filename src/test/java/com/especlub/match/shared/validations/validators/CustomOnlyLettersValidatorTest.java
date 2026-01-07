package com.especlub.match.shared.validations.validators;

import com.especlub.match.shared.validations.annotations.CustomOnlyLetters;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomOnlyLettersValidatorTest {

    private static Validator validator;

    static class TestClass {
        @CustomOnlyLetters
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
    void validOnlyLettersTest() {
        TestClass obj = new TestClass("Juan Pérez");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), "No debe haber violaciones para solo letras y espacios");
    }

    @Test
    void validWithAccentsTest() {
        TestClass obj = new TestClass("Ángel María");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), "No debe haber violaciones para letras con tildes");
    }

    @Test
    void invalidWithNumbersTest() {
        TestClass obj = new TestClass("Juan123");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty(), "Debe haber violaciones para valores con números");
        assertEquals("El valor solo puede contener letras (mayúsculas, minúsculas, con tildes) y espacios", violations.iterator().next().getMessage());
    }

    @Test
    void invalidWithSpecialCharactersTest() {
        TestClass obj = new TestClass("Juan@Pérez");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty(), "Debe haber violaciones para valores con caracteres especiales");
        assertEquals("El valor solo puede contener letras (mayúsculas, minúsculas, con tildes) y espacios", violations.iterator().next().getMessage());
    }

    @Test
    void validEmptyStringTest() {
        TestClass obj = new TestClass("");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), "No debe haber violaciones para cadena vacía (si así lo define la lógica)");
    }
}
