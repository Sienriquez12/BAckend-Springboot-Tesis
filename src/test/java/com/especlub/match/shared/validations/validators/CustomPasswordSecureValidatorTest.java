package com.especlub.match.shared.validations.validators;

import com.especlub.match.shared.validations.annotations.CustomPasswordSecure;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomPasswordSecureValidatorTest {

    private static Validator validator;

    static class TestClass {
        @CustomPasswordSecure
        String password;

        TestClass(String password) {
            this.password = password;
        }
    }

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validPasswordTest() {
        TestClass obj = new TestClass("Abcdef1!");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), "No debe haber violaciones para una contraseña válida");
    }

    @Test
    void tooShortPasswordTest() {
        TestClass obj = new TestClass("Ab1!");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty(), "Debe haber violaciones para contraseña muy corta");
        assertEquals("La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial.", violations.iterator().next().getMessage());
    }

    @Test
    void noUppercasePasswordTest() {
        TestClass obj = new TestClass("abcdef1!");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty(), "Debe haber violaciones para contraseña sin mayúscula");
        assertEquals("La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial.", violations.iterator().next().getMessage());
    }

    @Test
    void noLowercasePasswordTest() {
        TestClass obj = new TestClass("ABCDEF1!");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty(), "Debe haber violaciones para contraseña sin minúscula");
        assertEquals("La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial.", violations.iterator().next().getMessage());
    }

    @Test
    void noNumberPasswordTest() {
        TestClass obj = new TestClass("Abcdefg!");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty(), "Debe haber violaciones para contraseña sin número");
        assertEquals("La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial.", violations.iterator().next().getMessage());
    }

    @Test
    void noSpecialCharacterPasswordTest() {
        TestClass obj = new TestClass("Abcdefg1");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty(), "Debe haber violaciones para contraseña sin carácter especial");
        assertEquals("La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial.", violations.iterator().next().getMessage());
    }
}
