package com.especlub.match.repositories;

import com.especlub.match.models.MultipleIntelligenceQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MultipleIntelligenceQuestionRepository extends JpaRepository<MultipleIntelligenceQuestion, Long> {
    List<MultipleIntelligenceQuestion> findAllByRecordStatusTrue();
}

