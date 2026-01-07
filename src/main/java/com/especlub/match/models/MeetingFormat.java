package com.especlub.match.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.text.Normalizer;
import java.util.Locale;

/**
 * Valores para el formato de reunión preferido por el estudiante.
 */
public enum MeetingFormat {
    PRESENCIAL("Presencial"),
    VIRTUAL("Virtual"),
    HIBRIDO("Híbrido");

    private final String label;

    MeetingFormat(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static MeetingFormat fromString(String value) {
        if (value == null) return null;
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .trim();
        switch (normalized) {
            case "presencial":
                return PRESENCIAL;
            case "virtual":
                return VIRTUAL;
            case "híbrido":
                return HIBRIDO;
            default:
                try {
                    return MeetingFormat.valueOf(value.trim().toUpperCase(Locale.ROOT));
                } catch (IllegalArgumentException ex) {
                    return null;
                }
        }
    }
}
