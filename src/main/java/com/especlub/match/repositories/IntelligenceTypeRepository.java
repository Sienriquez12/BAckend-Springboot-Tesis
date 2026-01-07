package com.especlub.match.repositories;

import com.especlub.match.models.IntelligenceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IntelligenceTypeRepository extends JpaRepository<IntelligenceType, Long> {
    List<IntelligenceType> findAllByRecordStatusTrue();
}

