package com.especlub.match.repositories;

import com.especlub.match.models.LlmInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LlmInteractionRepository extends JpaRepository<LlmInteraction, Long> {
    List<LlmInteraction> findAllByUserId(Long userId);
    List<LlmInteraction> findAllByStudentSurveyId(Long surveyId);
}

