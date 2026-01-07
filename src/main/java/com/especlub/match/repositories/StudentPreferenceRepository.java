package com.especlub.match.repositories;

import com.especlub.match.models.StudentPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentPreferenceRepository extends JpaRepository<StudentPreference, Long> {
    List<StudentPreference> findAllByStudentIdAndRecordStatusTrue(Long studentId);
}

