package com.especlub.match.repositories;

import com.especlub.match.models.ClubReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClubReasonRepository extends JpaRepository<ClubReason, Long> {

    List<ClubReason> findAllByRecordStatusTrue();

    List<ClubReason> findAllByIdInAndRecordStatusTrue(Collection<Long> ids);

    Optional<ClubReason> findByIdAndRecordStatusTrue(Long id);

    Optional<ClubReason> findByNameIgnoreCase(String name);
}

