package com.especlub.match.services.interfaces;

import com.especlub.match.models.MultipleIntelligenceQuestion;
import com.especlub.match.dto.request.CreateStudentMiAnswersRequestDto;
import com.especlub.match.dto.response.StudentMiResultDto;
import com.especlub.match.dto.response.StudentMiAnswerDto;

import java.util.List;
import java.util.Optional;

public interface StudentMiService {
    List<MultipleIntelligenceQuestion> getAllQuestions();
    Optional<MultipleIntelligenceQuestion> getQuestionById(Long id);

    // Nuevo: almacenar respuestas MI para un usuario y devolver resumen de resultados
    StudentMiResultDto saveAnswersForUser(Long userInfoId, CreateStudentMiAnswersRequestDto dto);

    // Nuevo: recuperar respuestas guardadas por userInfoId con el texto de la pregunta
    List<StudentMiAnswerDto> getAnswersByUser(Long userInfoId);
}
