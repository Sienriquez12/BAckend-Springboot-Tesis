package com.especlub.match.repositories;

import com.especlub.match.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByIdAndRecordStatusTrue(Long id);

    // Nuevo: buscar estudiante por userInfo id
    Optional<Student> findByUserInfo_IdAndRecordStatusTrue(Long userInfoId);
}
