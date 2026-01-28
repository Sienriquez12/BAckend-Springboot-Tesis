package com.especlub.match.controller.admin;

import com.especlub.match.docs.AdminEventControllerDoc;
import com.especlub.match.dto.request.CreateEventRequestDto;
import com.especlub.match.dto.request.UpdateEventRequestDto;
import com.especlub.match.dto.response.EventAdminDto;
import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.dto.response.UserInfoDto;
import com.especlub.match.services.interfaces.AdminEventService;
import com.especlub.match.services.interfaces.EventNotificationService;
import com.especlub.match.services.interfaces.AuthService;
import com.especlub.match.shared.utils.RolePermissions;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/events")
@RequiredArgsConstructor
@PreAuthorize(RolePermissions.ADMIN_GENERAL)
@Validated
public class AdminEventController implements AdminEventControllerDoc {

    private final AdminEventService adminEventService;
    private final EventNotificationService eventNotificationService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<JsonDtoResponse<EventAdminDto>> create(HttpServletRequest request, @Valid @RequestBody CreateEventRequestDto dto) {
        // extract current user from cookie/JWT
        com.especlub.match.models.UserInfo currentUser = authService.validateUserJWT(request);
        Long userInfoId = currentUser.getId();
        log.info("El usuario es: {}", currentUser.getUsername());

        EventAdminDto created = adminEventService.create(dto, userInfoId);
        return JsonDtoResponse.created("Evento creado", created).toResponseEntity();
    }

    @GetMapping
    public ResponseEntity<JsonDtoResponse<List<EventAdminDto>>> listAllActive() {
        List<EventAdminDto> events = adminEventService.findAllActive();
        events.forEach(event -> {
            if (event.getCreatedByUserInfo() != null) {
                UserInfoDto userInfo = authService.getUserInfoById(event.getCreatedByUserInfo().getId());
                event.setCreatedByUserInfo(userInfo);
            }
        });
        return JsonDtoResponse.ok("Events retrieved", events).toResponseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JsonDtoResponse<EventAdminDto>> getById(@PathVariable Long id) {
        EventAdminDto dto = adminEventService.findById(id);
        if (dto == null) return JsonDtoResponse.<EventAdminDto>notFound("Event not found").toResponseEntity();
        if (dto.getCreatedByUserInfo() != null) {
            UserInfoDto userInfo = authService.getUserInfoById(dto.getCreatedByUserInfo().getId());
            dto.setCreatedByUserInfo(userInfo);
        }
        return JsonDtoResponse.ok("Event retrieved", dto).toResponseEntity();
    }

    @PutMapping("/{id}")
    public ResponseEntity<JsonDtoResponse<EventAdminDto>> update(@PathVariable Long id, @Valid @RequestBody UpdateEventRequestDto dto) {
        EventAdminDto updated = adminEventService.update(id, dto);
        return JsonDtoResponse.ok("Event updated", updated).toResponseEntity();
    }

    @PostMapping("/{id}/notify")
    public ResponseEntity<JsonDtoResponse<Void>> notifyMembers(@PathVariable("id") Long eventId) {
        try {
            return JsonDtoResponse.ok("Notificaciones encoladas", eventNotificationService.notifyEventToMembers(eventId)).toResponseEntity();
        } catch (Exception ex) {
            log.error("Error while notifying members for event id={}: {}", eventId, ex.getMessage(), ex);
            return JsonDtoResponse.<Void>error("Error al encolar notificaciones", 500).toResponseEntity();
        }
    }
}