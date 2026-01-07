package com.especlub.match.repositories;

import com.especlub.match.models.StudentMiAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentMiAnswerRepository extends JpaRepository<StudentMiAnswer, Long> {
    List<StudentMiAnswer> findAllByStudent_Id(Long studentId);

    // Método que trae las respuestas junto con la pregunta (join fetch) para evitar LazyInitializationException
    @Query("select a from StudentMiAnswer a join fetch a.question q where a.student.id = :studentId")
    List<StudentMiAnswer> findAllByStudentIdWithQuestion(@Param("studentId") Long studentId);
}
