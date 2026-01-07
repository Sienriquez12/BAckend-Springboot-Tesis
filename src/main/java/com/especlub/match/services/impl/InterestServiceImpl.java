package com.especlub.match.services.impl;

import com.especlub.match.models.Interest;
import com.especlub.match.repositories.InterestRepository;
import com.especlub.match.services.interfaces.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InterestServiceImpl implements InterestService {

    private final InterestRepository interestRepository;

    @Override
    public List<Interest> listAllActive() {
        return interestRepository.findAllByRecordStatusTrue();
    }

    @Override
    public Optional<Interest> findById(Long id) {
        return interestRepository.findByIdAndRecordStatusTrue(id);
    }
}

