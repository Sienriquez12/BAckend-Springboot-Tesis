package com.especlub.match.services.impl;

import com.especlub.match.models.ClubReason;
import com.especlub.match.repositories.ClubReasonRepository;
import com.especlub.match.services.interfaces.ClubReasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClubReasonServiceImpl implements ClubReasonService {

    private final ClubReasonRepository clubReasonRepository;

    @Override
    public List<ClubReason> listAllActive() {
        return clubReasonRepository.findAllByRecordStatusTrue();
    }

    @Override
    public Optional<ClubReason> findById(Long id) {
        return clubReasonRepository.findByIdAndRecordStatusTrue(id);
    }

    @Override
    public Optional<ClubReason> findByName(String name) {
        return clubReasonRepository.findByNameIgnoreCase(name);
    }
}

