package com.especlub.match.shared.validations.annotations;

import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CustomAlfanumericAnnotationTest {
    @Test
    void testAnnotationTargets() {
        Target target = CustomAlfanumeric.class.getAnnotation(Target.class);
        assertNotNull(target, "Debe tener la anotación @Target");
        ElementType[] types = target.value();
        assertTrue(Arrays.asList(types).contains(ElementType.FIELD), "Debe aplicarse a campos");
        assertTrue(Arrays.asList(types).contains(ElementType.PARAMETER), "Debe aplicarse a parámetros");
    }

    @Test
    void testAnnotationRetention() {
        Retention retention = CustomAlfanumeric.class.getAnnotation(Retention.class);
        assertNotNull(retention, "Debe tener la anotación @Retention");
        assertEquals(RetentionPolicy.RUNTIME, retention.value(), "La retención debe ser RUNTIME");
    }

    @Test
    void testAnnotationDocumented() {
        assertTrue(CustomAlfanumeric.class.isAnnotationPresent(Documented.class), "Debe estar documentada");
    }

    @Test
    void testDefaultMessage() throws NoSuchMethodException {
        Method messageMethod = CustomAlfanumeric.class.getMethod("message");
        assertEquals("Solo se permiten letras latinas, números y espacios", messageMethod.getDefaultValue(), "El mensaje por defecto debe ser el esperado");
    }
}

