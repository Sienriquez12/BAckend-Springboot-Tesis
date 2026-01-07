package com.especlub.match.shared.validations.validators;

import com.especlub.match.shared.validations.annotations.CustomValidEmail;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomValidEmailValidatorTest {

    private static Validator validator;

    static class TestClass {
        //@Email
        @CustomValidEmail
        String email;

        TestClass(String email) {
            this.email = email;
        }
    }

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validEmailTest() {
        TestClass obj = new TestClass("121121212@espe.edu");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), "No debe haber violaciones para un email válido");
    }

    @Test
    void validEmailWithSubdomainTest() {
        TestClass obj = new TestClass("usuario@mail.dominio.com");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), "No debe haber violaciones para un email con subdominio válido");
    }

    @Test
    void invalidEmailNoAtTest() {
        TestClass obj = new TestClass("usuariodominio.com");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty(), "Debe haber violaciones para email sin @");
        assertEquals("El email debe ser válido basado en nombre@dominio.extension", violations.iterator().next().getMessage());
    }

    @Test
    void invalidEmailNoDomainTest() {
        TestClass obj = new TestClass("usuario@");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty(), "Debe haber violaciones para email sin dominio");
        assertEquals("El email debe ser válido basado en nombre@dominio.extension", violations.iterator().next().getMessage());
    }

    @Test
    void invalidEmailNoExtensionTest() {
        TestClass obj = new TestClass("usuario@dominio");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty(), "Debe haber violaciones para email sin extensión");
        assertEquals("El email debe ser válido basado en nombre@dominio.extension", violations.iterator().next().getMessage());
    }
}

