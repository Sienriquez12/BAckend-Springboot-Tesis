package com.especlub.match.repositories;

import com.especlub.match.models.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    List<Club> findAllByRecordStatusTrue();
    Optional<Club> findByIdAndRecordStatusTrue(Long id);

}