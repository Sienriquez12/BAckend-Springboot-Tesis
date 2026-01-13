package com.especlub.match.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.Normalizer;

/**
 * Enum para los formatos de reunión de clubes.
 */
@AllArgsConstructor
@Getter
public enum MeetingFormat {
    PRESENCIAL("Presencial"),
    VIRTUAL("Virtual"),
    HIBRIDO("Híbrido");

    private final String label;

    @JsonValue
    public String getLabel() {
        return label;
    }

    /**
     * Método robusto para convertir string a MeetingFormat.
     * Normaliza el input para aceptar diferentes variaciones.
     */
    @JsonCreator
    public static MeetingFormat fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        // Normalizar el string: remover acentos, convertir a minúsculas, remover espacios
        String normalized = removeAccents(value.trim().toLowerCase());

        return switch (normalized) {
            case "presencial" -> PRESENCIAL;
            case "virtual" -> VIRTUAL;
            case "hibrido", "híbrido" -> HIBRIDO;
            default -> null;
        };
    }

    /**
     * Método auxiliar para remover acentos de un string.
     */
    private static String removeAccents(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    @Override
    public String toString() {
        return label;
    }
}
