package com.especlub.match.repositories;

import com.especlub.match.models.ClubProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubProfileRepository extends JpaRepository<ClubProfile, Long> {
    List<ClubProfile> findAllByIsActiveForRecommendationTrue();
    Optional<ClubProfile> findByIdAndClub_RecordStatusTrue(Long id);
    Optional<ClubProfile> findByClub_Id(Long clubId);
}

