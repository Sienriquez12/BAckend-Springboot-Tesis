package com.especlub.match.shared.validations.annotations;

import com.especlub.match.shared.validations.validators.CustomValidEmailValidator;
import org.junit.jupiter.api.Test;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;
import jakarta.validation.Constraint;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

class CustomValidEmailAnnotationTest {
    @Test
    void testAnnotationTargets() {
        Target target = CustomValidEmail.class.getAnnotation(Target.class);
        assertNotNull(target, "Debe tener la anotación @Target");
        ElementType[] types = target.value();
        assertTrue(java.util.Arrays.asList(types).contains(ElementType.FIELD), "Debe aplicarse a campos");
        assertTrue(java.util.Arrays.asList(types).contains(ElementType.PARAMETER), "Debe aplicarse a parámetros");
    }

    @Test
    void testAnnotationRetention() {
        Retention retention = CustomValidEmail.class.getAnnotation(Retention.class);
        assertNotNull(retention, "Debe tener la anotación @Retention");
        assertEquals(RetentionPolicy.RUNTIME, retention.value(), "La retención debe ser RUNTIME");
    }

    @Test
    void testAnnotationDocumented() {
        assertTrue(CustomValidEmail.class.isAnnotationPresent(Documented.class), "Debe estar documentada");
    }

    @Test
    void testConstraintValidatorClass() {
        Constraint constraint = CustomValidEmail.class.getAnnotation(Constraint.class);
        assertNotNull(constraint, "Debe tener la anotación @Constraint");
        assertEquals(CustomValidEmailValidator.class, constraint.validatedBy()[0], "El validador debe ser CustomValidEmailValidator");
    }

    @Test
    void testDefaultMessage() throws NoSuchMethodException {
        Method messageMethod = CustomValidEmail.class.getMethod("message");
        assertEquals("El email debe ser válido basado en nombre@dominio.extension", messageMethod.getDefaultValue(), "El mensaje por defecto debe ser el esperado");
    }
}

