package com.especlub.match.services;

import com.especlub.match.dto.response.RecommendationListDto;
import com.especlub.match.dto.response.StudentSurveyResponseDto;
import com.especlub.match.models.StudentSurvey;

public interface StudentSurveyService {
    StudentSurvey saveSurvey(Long studentId, com.especlub.match.dto.request.CreateSurveyRequestDto dto);

    StudentSurvey generateRecommendation(Long studentId);

    RecommendationListDto recommendClubs(Long studentId);

    StudentSurveyResponseDto toResponseDto(StudentSurvey survey);
}
