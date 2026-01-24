package com.especlub.match.services.impl;

import com.especlub.match.dto.request.CreateEventRequestDto;
import com.especlub.match.dto.request.UpdateEventRequestDto;
import com.especlub.match.dto.response.EventAdminDto;
import com.especlub.match.models.Club;
import com.especlub.match.models.Event;
import com.especlub.match.models.UserInfo;
import com.especlub.match.models.ClubMember;
import com.especlub.match.models.Student;
import com.especlub.match.repositories.ClubRepository;
import com.especlub.match.repositories.EventRepository;
import com.especlub.match.repositories.UserInfoRepository;
import com.especlub.match.repositories.ClubMemberRepository;
import com.especlub.match.repositories.StudentRepository;
import com.especlub.match.security.jwt.JwtProvider;
import com.especlub.match.services.interfaces.AdminEventService;
import com.especlub.match.shared.exceptions.CustomExceptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;

import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminEventServiceImpl implements AdminEventService {

    private final EventRepository eventRepository;
    private final ClubRepository clubRepository;
    private final UserInfoRepository userInfoRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final StudentRepository studentRepository;
    private final JwtProvider jwtProvider;

    @Override
    @Transactional
    public EventAdminDto create(CreateEventRequestDto dto, Long createdByUserInfoId) {
        Club club = clubRepository.findByIdAndRecordStatusTrue(dto.getClubId())
                .orElseThrow(() -> new CustomExceptions("Club not found or inactive", 404));

        UserInfo creator = userInfoRepository.findByIdAndRecordStatusTrue(createdByUserInfoId)
                .orElseThrow(() -> new CustomExceptions("Creator user not found or inactive", 404));

        if (dto.getEndAt() != null && dto.getEndAt().isBefore(dto.getStartAt())) {
            throw new CustomExceptions("endAt must be after startAt", 400);
        }

        Event event = Event.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startAt(dto.getStartAt())
                .endAt(dto.getEndAt())
                .location(dto.getLocation())
                .virtualLink(dto.getVirtualLink())
                .club(club)
                .createdBy(creator)
                .recordStatus(true)
                .build();

        Event saved = eventRepository.save(event);

        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventAdminDto> findAllActiveByUser(String jwt) {
        log.debug("findAllActiveByUser: starting, jwt present={}", jwt != null && !jwt.isBlank());
        if (jwt == null || jwt.isBlank()) {
            log.warn("findAllActiveByUser: jwt empty");
            throw new CustomExceptions("Token no proporcionado", HttpStatus.UNAUTHORIZED.value());
        }
        String username = jwtProvider.getNombreUsuarioFromToken(jwt);
        log.debug("findAllActiveByUser: username extracted={}", username);
        UserInfo user = userInfoRepository.findByUsernameAndRecordStatusTrue(username);
        if (user == null) {
            log.warn("findAllActiveByUser: user not found or inactive for username={}", username);
            throw new CustomExceptions("Usuario no encontrado o inactivo", org.springframework.http.HttpStatus.NOT_FOUND.value());
        }

        // Events created by the user
        List<Event> createdEvents = eventRepository.findAllByRecordStatusTrueAndCreatedByIdOrderByStartAtAsc(user.getId());
        log.debug("findAllActiveByUser: createdEvents count={}", createdEvents.size());

        // Try to find Student associated to this userInfo
        List<Long> memberClubIds = List.of();
        Optional<Student> maybeStudent = studentRepository.findByUserInfo_IdAndRecordStatusTrue(user.getId());
        if (maybeStudent.isPresent()) {
            Student student = maybeStudent.get();
            log.debug("findAllActiveByUser: found studentId={} for userInfoId={}", student.getId(), user.getId());
            List<ClubMember> memberships = clubMemberRepository.findAllByStudentIdAndRecordStatusTrue(student.getId());
            memberClubIds = memberships.stream()
                    .map(cm -> cm.getClub() != null ? cm.getClub().getId() : null)
                    .filter(id -> id != null)
                    .collect(Collectors.toList());
            log.debug("findAllActiveByUser: memberships count={}, clubIds={}", memberships.size(), memberClubIds);
        } else {
            log.debug("findAllActiveByUser: no student record for userInfoId={}", user.getId());
        }

        List<Event> memberClubEvents = List.of();
        if (!memberClubIds.isEmpty()) {
            memberClubEvents = eventRepository.findAllByRecordStatusTrueAndClubIdInOrderByStartAtAsc(memberClubIds);
            log.debug("findAllActiveByUser: memberClubEvents count={}", memberClubEvents.size());
        }

        // Merge createdEvents and memberClubEvents preserving order and avoiding duplicates
        LinkedHashMap<Long, Event> map = new LinkedHashMap<>();
        for (Event e : createdEvents) {
            map.put(e.getId(), e);
        }
        for (Event e : memberClubEvents) {
            map.putIfAbsent(e.getId(), e);
        }

        List<Event> merged = map.values().stream().collect(Collectors.toList());

        log.debug("findAllActiveByUser: merged events count={}", merged.size());

        return merged.stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventAdminDto> findAllActive() {
        List<Event> events = eventRepository.findAllByRecordStatusTrueOrderByStartAtAsc();
        return events.stream().map(this::toDto).toList();
    }

    @Override
    @Transactional
    public EventAdminDto update(Long id, UpdateEventRequestDto dto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions("Event not found", 404));

        if (dto.getEndAt() != null && dto.getEndAt().isBefore(dto.getStartAt())) {
            throw new CustomExceptions("endAt must be after startAt", 400);
        }

        // Update fields
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setStartAt(dto.getStartAt());
        event.setEndAt(dto.getEndAt());
        event.setLocation(dto.getLocation());
        event.setVirtualLink(dto.getVirtualLink());
        if (dto.getRecordStatus() != null) event.setRecordStatus(dto.getRecordStatus());

        Event saved = eventRepository.save(event);
        return toDto(saved);
    }

    @Override
    public EventAdminDto findById(Long id) {
        Optional<Event> opt = eventRepository.findById(id);
        return opt.map(this::toDto).orElse(null);
    }

    private EventAdminDto toDto(Event e) {
        if (e == null) return null;
        return EventAdminDto.builder()
                .id(e.getId())
                .title(e.getTitle())
                .description(e.getDescription())
                .startAt(e.getStartAt())
                .endAt(e.getEndAt())
                .location(e.getLocation())
                .virtualLink(e.getVirtualLink())
                .clubId(e.getClub() != null ? e.getClub().getId() : null)
                .createdByUserInfoId(e.getCreatedBy() != null ? e.getCreatedBy().getId() : null)
                .recordStatus(e.getRecordStatus())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}

