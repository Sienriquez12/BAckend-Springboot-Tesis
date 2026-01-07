package com.especlub.match.services.interfaces;

import com.especlub.match.models.Interest;

import java.util.List;
import java.util.Optional;

public interface InterestService {
    List<Interest> listAllActive();
    Optional<Interest> findById(Long id);
}

