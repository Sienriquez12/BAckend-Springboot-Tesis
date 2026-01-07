package com.especlub.match.shared.validations.validators;

import com.especlub.match.shared.validations.annotations.CustomPassportNumber;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.constraints.NotBlank;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomPassportNumberValidatorTest {

    private static Validator validator;

    static class TestClass {
        @NotBlank(message = "El número de pasaporte no puede estar vacío.")
        @CustomPassportNumber
        String passportNumber;

        TestClass(String passportNumber) {
            this.passportNumber = passportNumber;
        }
    }

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validPassportNumberTest() {
        TestClass obj = new TestClass("A123456"); // 7 caracteres alfanuméricos
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), "No debe haber violaciones para un pasaporte válido");
    }

    @Test
    void tooShortPassportNumberTest() {
        TestClass obj = new TestClass("A123"); // 5 caracteres
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty(), "Debe haber violaciones para un pasaporte muy corto");
        assertEquals("El número de pasaporte no es válido. Debe ser alfanumérico y tener entre 6 y 9 caracteres.", violations.iterator().next().getMessage());
    }

    @Test
    void tooLongPassportNumberTest() {
        TestClass obj = new TestClass("A123456789"); // 10 caracteres
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty(), "Debe haber violaciones para un pasaporte muy largo");
        assertEquals("El número de pasaporte no es válido. Debe ser alfanumérico y tener entre 6 y 9 caracteres.", violations.iterator().next().getMessage());
    }

    @Test
    void passportNumberWithSpecialCharactersTest() {
        TestClass obj = new TestClass("A12-456"); // Caracter especial
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty(), "Debe haber violaciones para caracteres especiales");
        assertEquals("El número de pasaporte no es válido. Debe ser alfanumérico y tener entre 6 y 9 caracteres.", violations.iterator().next().getMessage());
    }

    @Test
    void emptyPassportNumberTest() {
        TestClass obj = new TestClass("");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty(), "Debe haber violaciones para cadena vacía");
        // Puede haber más de una violación, así que verificamos los mensajes
        boolean hasCustomMessage = violations.stream().anyMatch(v -> v.getMessage().equals("El número de pasaporte no es válido. Debe ser alfanumérico y tener entre 6 y 9 caracteres."));
        boolean hasBlankMessage = violations.stream().anyMatch(v -> v.getMessage().equals("El número de pasaporte no puede estar vacío."));
        assertTrue(hasCustomMessage || hasBlankMessage, "Debe haber un mensaje de error adecuado para cadena vacía");
    }
}
