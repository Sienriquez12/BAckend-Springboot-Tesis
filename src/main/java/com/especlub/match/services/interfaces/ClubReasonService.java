package com.especlub.match.services.interfaces;

import com.especlub.match.models.ClubReason;

import java.util.List;
import java.util.Optional;

public interface ClubReasonService {
    List<ClubReason> listAllActive();
    Optional<ClubReason> findById(Long id);
    Optional<ClubReason> findByName(String name);
}

