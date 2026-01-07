package com.especlub.match.repositories;

import com.especlub.match.models.SoftSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SoftSkillRepository extends JpaRepository<SoftSkill, Long> {
    List<SoftSkill> findAllByRecordStatusTrue();
    List<SoftSkill> findAllByIdInAndRecordStatusTrue(Collection<Long> ids);
    Optional<SoftSkill> findByIdAndRecordStatusTrue(Long id);
}
