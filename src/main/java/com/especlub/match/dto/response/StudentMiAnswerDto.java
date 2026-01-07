package com.especlub.match.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentMiAnswerDto {
    // Reemplazamos questionId por el texto de la pregunta para que la API devuelva
    // la pregunta legible en lugar del identificador.
    private String questionText;
    private Integer score; // 1..5
}