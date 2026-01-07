package com.especlub.match.services.impl;


import com.especlub.match.dto.response.EventAdminDto;
import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.repositories.UserInfoRepository;
import com.especlub.match.services.interfaces.AdminEventService;
import com.especlub.match.services.interfaces.ClubMemberService;
import com.especlub.match.services.interfaces.EmailServiceAsync;
import com.especlub.match.services.interfaces.EventNotificationService;
import com.especlub.match.shared.exceptions.CustomExceptions;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EventNotificationServiceImpl implements EventNotificationService {

    private static final Logger log = LoggerFactory.getLogger(EventNotificationServiceImpl.class);

    private final EmailServiceAsync emailServiceAsync;
    private final UserInfoRepository userInfoRepository;
    private final AdminEventService adminEventService;
    private final ClubMemberService clubMemberService;

    private static final String TEMPLATE_NAME = "event-invitation-email";
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault());

    @Override
    public Void notifyEventToMembers(Long eventId) {
        EventAdminDto event = adminEventService.findById(eventId);
        if (event == null) {
            log.warn("Event not found for id={}", eventId);
            throw new CustomExceptions("Evento no encontrado", 404);
        }

        Long clubId = event.getClubId();
        if (clubId == null) {
            log.info("Event id={} has no associated club, nothing to notify", eventId);
            throw  new CustomExceptions("Evento no asociado", 404);
        }

        Map<String, String> participantNameByEmail = clubMemberService.findActiveMemberFullNamesByClubId(clubId);
        if (participantNameByEmail == null || participantNameByEmail.isEmpty()) {
            log.info("No active members for clubId={} (event id={})", clubId, eventId);
            throw new CustomExceptions("No hay miembros activos para notificar", 404);
        }

        List<String> recipients = new ArrayList<>(participantNameByEmail.keySet());
        if (recipients.isEmpty()) {
            log.info("No event or recipients to notify.");
            throw new CustomExceptions("No hay evento para notificar", 404);
        }

        LocalDateTime startAt = event.getStartAt();
        if (startAt == null) {
            log.warn("Event start date is null, skipping notifications for event id={}", event.getId());
            throw new CustomExceptions("Fecha de inicio del evento no definida", 400);
        }

        LocalDate eventDate = startAt.toLocalDate();
        LocalDate today = LocalDate.now();

        if (eventDate.isBefore(today)) {
            log.info("Event date {} already passed (today={}), skipping notifications for event id={}", eventDate, today, event.getId());
            throw new CustomExceptions("La fecha del evento ya pasó", 400);
        }

        String subject = "Invitación: " + (event.getTitle() != null ? event.getTitle() : "Evento");

        for (String to : recipients) {
            try {
                String participantName = participantNameByEmail.get(to);
                Map<String, Object> vars = buildTemplateVariables(event, participantName);
                emailServiceAsync.sendHtmlEmail(subject, to, TEMPLATE_NAME, vars);
                log.info("Notification queued for {} (event id={})", to, event.getId());
            } catch (Exception ex) {
                log.error("Failed to queue email to {} for event id={}: {}", to, event.getId(), ex.getMessage(), ex);
            }
        }
        return null;
    }

    private Map<String, Object> buildTemplateVariables(EventAdminDto eventDto, String participantName) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("participantName", participantName != null ? participantName : "participante");
        vars.put("eventName", eventDto.getTitle() != null ? eventDto.getTitle() : "Nombre del Evento");
        vars.put("eventLocation", eventDto.getLocation() != null ? eventDto.getLocation() : "Por definir");

        LocalDateTime startAt = eventDto.getStartAt();
        vars.put("eventDate", startAt != null ? startAt.format(DATE_FMT) : "Por confirmar");
        vars.put("eventTime", startAt != null ? startAt.format(TIME_FMT) : "Por definir");

        String organizerName = "Organizador";
        Long creatorId = eventDto.getCreatedByUserInfoId();
        if (creatorId != null) {
            organizerName = userInfoRepository.findByIdAndRecordStatusTrue(creatorId)
                    .map(u -> {
                        String first = safeTrim(u.getNames());
                        String last = safeTrim(u.getSurnames());
                        String full = (first + " " + last).trim();
                        if (!full.isEmpty()) return full;
                        if (u.getUsername() != null && !u.getUsername().isBlank()) return u.getUsername();
                        if (u.getEmail() != null && !u.getEmail().isBlank()) return u.getEmail();
                        return "Organizador";
                    })
                    .orElse("Organizador");
        }
        vars.put("organizerName", organizerName);

        vars.put("eventDescription", eventDto.getDescription());
        vars.put("eventLink", eventDto.getVirtualLink() != null ? eventDto.getVirtualLink() : "#");
        vars.put("organizationName", "ESPE Clubs Match");

        return vars;
    }

    private static String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }
}