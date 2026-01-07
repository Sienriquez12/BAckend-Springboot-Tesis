package com.especlub.match.repositories;

import com.especlub.match.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByIdAndRecordStatusTrue(Long id);
    List<Event> findAllByRecordStatusTrueOrderByStartAtAsc();
    List<Event> findAllByRecordStatusTrueAndCreatedByIdOrderByStartAtAsc(Long createdById);
    List<Event> findAllByRecordStatusTrueAndClubIdInOrderByStartAtAsc(List<Long> clubIds);

}