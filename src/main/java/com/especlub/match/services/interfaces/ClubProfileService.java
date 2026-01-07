package com.especlub.match.services.interfaces;

import com.especlub.match.models.ClubProfile;

import java.util.List;
import java.util.Optional;

public interface ClubProfileService {
    List<ClubProfile> listActiveForRecommendation();
    Optional<ClubProfile> findById(Long id);
    Optional<ClubProfile> findByClubId(Long clubId);
}

