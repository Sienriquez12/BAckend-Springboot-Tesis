package com.especlub.match.services.interfaces;

import com.especlub.match.models.SoftSkill;

import java.util.List;
import java.util.Optional;

public interface SoftSkillService {
    List<SoftSkill> listAllActive();
    Optional<SoftSkill> findById(Long id);
}

