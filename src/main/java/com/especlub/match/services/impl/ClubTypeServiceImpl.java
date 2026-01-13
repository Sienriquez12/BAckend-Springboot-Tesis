package com.especlub.match.services.impl;

import com.especlub.match.dto.response.ClubTypeDto;
import com.especlub.match.models.ClubType;
import com.especlub.match.repositories.ClubTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClubTypeServiceImpl {

    private final ClubTypeRepository clubTypeRepository;

    @Transactional(readOnly = true)
    public List<ClubType> listAllActive() {
        log.debug("listAllActive: retrieving all active club types");
        return clubTypeRepository.findAllByRecordStatusTrueOrderByOrderIndexAsc();
    }

    @Transactional(readOnly = true)
    public Optional<ClubType> findById(Long id) {
        log.debug("findById: searching for club type with id={}", id);
        return clubTypeRepository.findByIdAndRecordStatusTrue(id);
    }

    @Transactional(readOnly = true)
    public Optional<ClubType> findByName(String name) {
        log.debug("findByName: searching for club type with name={}", name);
        return clubTypeRepository.findByNameAndRecordStatusTrue(name);
    }

    public ClubTypeDto toDto(ClubType clubType) {
        return ClubTypeDto.builder()
                .id(clubType.getId())
                .name(clubType.getName())
                .description(clubType.getDescription())
                .orderIndex(clubType.getOrderIndex())
                .build();
    }
}
