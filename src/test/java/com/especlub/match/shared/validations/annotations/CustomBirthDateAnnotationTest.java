package com.especlub.match.shared.validations.annotations;

import org.junit.jupiter.api.Test;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;
import jakarta.validation.Constraint;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

class CustomBirthDateAnnotationTest {
    @Test
    void testAnnotationTargets() {
        Target target = CustomBirthDate.class.getAnnotation(Target.class);
        assertNotNull(target, "Debe tener la anotación @Target");
        ElementType[] types = target.value();
        assertTrue(java.util.Arrays.asList(types).contains(ElementType.FIELD), "Debe aplicarse a campos");
        assertTrue(java.util.Arrays.asList(types).contains(ElementType.PARAMETER), "Debe aplicarse a parámetros");
    }

    @Test
    void testAnnotationRetention() {
        Retention retention = CustomBirthDate.class.getAnnotation(Retention.class);
        assertNotNull(retention, "Debe tener la anotación @Retention");
        assertEquals(RetentionPolicy.RUNTIME, retention.value(), "La retención debe ser RUNTIME");
    }

    @Test
    void testAnnotationDocumented() {
        assertTrue(CustomBirthDate.class.isAnnotationPresent(Documented.class), "Debe estar documentada");
    }

    @Test
    void testDefaultMessage() throws NoSuchMethodException {
        Method messageMethod = CustomBirthDate.class.getMethod("message");
        assertEquals("La edad debe ser mayor o igual a 18 y menor o igual a 100 años", messageMethod.getDefaultValue(), "El mensaje por defecto debe ser el esperado");
    }
}
