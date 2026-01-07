package com.especlub.match.repositories;

import com.especlub.match.models.StudentSurvey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentSurveyRepository extends JpaRepository<StudentSurvey, Long> {
    Optional<StudentSurvey> findByStudentId(Long studentId);
}

