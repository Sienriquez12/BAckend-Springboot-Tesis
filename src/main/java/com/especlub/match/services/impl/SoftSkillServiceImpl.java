package com.especlub.match.services.impl;

import com.especlub.match.models.SoftSkill;
import com.especlub.match.repositories.SoftSkillRepository;
import com.especlub.match.services.interfaces.SoftSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SoftSkillServiceImpl implements SoftSkillService {

    private final SoftSkillRepository softSkillRepository;

    @Override
    public List<SoftSkill> listAllActive() {
        return softSkillRepository.findAllByRecordStatusTrue();
    }

    @Override
    public Optional<SoftSkill> findById(Long id) {
        return softSkillRepository.findByIdAndRecordStatusTrue(id);
    }
}

