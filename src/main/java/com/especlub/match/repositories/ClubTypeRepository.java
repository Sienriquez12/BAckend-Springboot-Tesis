package com.especlub.match.repositories;

import com.especlub.match.models.ClubType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubTypeRepository extends JpaRepository<ClubType, Long> {
    List<ClubType> findAllByRecordStatusTrueOrderByOrderIndexAsc();
    Optional<ClubType> findByIdAndRecordStatusTrue(Long id);
    Optional<ClubType> findByNameAndRecordStatusTrue(String name);
    boolean existsByNameAndRecordStatusTrue(String name);
}
