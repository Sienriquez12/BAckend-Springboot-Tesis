package com.especlub.match.repositories;

import com.especlub.match.models.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {

    List<Interest> findAllByRecordStatusTrue();

    List<Interest> findAllByIdInAndRecordStatusTrue(Collection<Long> ids);

    Optional<Interest> findByIdAndRecordStatusTrue(Long id);
}