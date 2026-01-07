package com.especlub.match.shared.validations.validators;

import com.especlub.match.shared.validations.annotations.CustomIpAddress;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomIpAddressValidatorTest {

    private static Validator validator;

    static class TestClass {
        @CustomIpAddress
        String ip;

        TestClass(String ip) {
            this.ip = ip;
        }
    }

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validIpv4Test() {
        TestClass obj = new TestClass("192.168.1.1");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), "No debe haber violaciones para IPv4 válido");
    }

    @Test
    void validIpv6Test() {
        TestClass obj = new TestClass("2001:db8::ff00:42:8329");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), "No debe haber violaciones para IPv6 válido");
    }

    @Test
    void invalidIpTest() {
        TestClass obj = new TestClass("999.999.999.999");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty(), "Debe haber violaciones para IP inválida");
        assertEquals("La dirección IP no es válida", violations.iterator().next().getMessage());
    }

    @Test
    void emptyIpTest() {
        TestClass obj = new TestClass("");
        Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), "No debe haber violaciones para cadena vacía (usa @NotBlank si quieres forzar)");
    }
}