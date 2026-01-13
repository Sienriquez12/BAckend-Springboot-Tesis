package com.especlub.match.controller.internal;

import com.especlub.match.docs.UserControllerDoc;
import com.especlub.match.dto.request.PasswordUpdateInternalRequestDto;
import com.especlub.match.dto.response.AdminUserSummaryDto;
import com.especlub.match.dto.response.EventAdminDto;
import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.dto.response.UserRolesDto;
import com.especlub.match.models.UserInfo;
import com.especlub.match.models.UserRole;
import com.especlub.match.services.impl.AuthServiceImpl;
import com.especlub.match.services.interfaces.AdminEventService;
import com.especlub.match.shared.exceptions.CustomExceptions;
import com.especlub.match.shared.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController implements UserControllerDoc {

    private final CookieUtils cookieUtils;
    private final AuthServiceImpl authService;
    private final AdminEventService adminEventService;

    @GetMapping("/me")
    public ResponseEntity<JsonDtoResponse<AdminUserSummaryDto>> getCurrentUser(HttpServletRequest request) {
        UserInfo currentUser = authService.validateUserJWT(request);
        List<UserRole> roles = currentUser.getRoles();
        List<String> roleNames = roles == null ? List.of() : roles.stream().map(UserRole::getName).toList();

        AdminUserSummaryDto summary = AdminUserSummaryDto.builder()
                .username(currentUser.getUsername())
                .email(currentUser.getEmail())
                .phone(currentUser.getPhone())
                .firstName(currentUser.getNames())
                .lastName(currentUser.getSurnames())
                .roles(roleNames)
                .build();
        return JsonDtoResponse.ok("Usuario actual obtenido", summary).toResponseEntity();
    }

    // New endpoint to return the roles of the current user
    @GetMapping("/roles")
    public ResponseEntity<JsonDtoResponse<UserRolesDto>> getCurrentUserRoles(HttpServletRequest request) {
        log.debug("getCurrentUserRoles: start");
        UserInfo currentUser = authService.validateUserJWT(request);
        List<UserRole> roles = currentUser.getRoles();
        List<String> roleNames = roles == null ? List.of() : roles.stream()
                .map(UserRole::getName)
                .toList();
        UserRolesDto dto = UserRolesDto.builder()
                .userId(currentUser.getId())
                .username(currentUser.getUsername())
                .roles(roleNames)
                .build();
        log.debug("getCurrentUserRoles: roles={}", roleNames);
        return JsonDtoResponse.ok("User roles retrieved", dto).toResponseEntity();
    }

    @PatchMapping("/internal/update-password")
    public ResponseEntity<JsonDtoResponse<Boolean>> updatePasswordInternal(@RequestBody @Valid PasswordUpdateInternalRequestDto dto, HttpServletRequest request) {
        String jwt = cookieUtils.extractTokenFromHeaderOrCookie(request);
        boolean result = authService.updatePasswordInternal(dto, jwt);
        return JsonDtoResponse.ok("Contraseña actualizada correctamente", result).toResponseEntity();
    }

    @GetMapping("/notifications/events/active")
    public ResponseEntity<JsonDtoResponse<List<EventAdminDto>>> getAllActive(HttpServletRequest request) {
        log.debug("getAllActive: start");
        String jwt  = cookieUtils.extractTokenFromHeaderOrCookie(request);
        log.debug("getAllActive: extracted jwt present={}", jwt != null && !jwt.isBlank());
        try {
            List<EventAdminDto> events = adminEventService.findAllActiveByUser(jwt);
            log.debug("getAllActive: events returned count={}", events == null ? 0 : events.size());
            return JsonDtoResponse.ok("Active events retrieved", events).toResponseEntity();
        } catch (CustomExceptions ce) {
            log.warn("getAllActive: CustomException statusCode={} msg={}", ce.getStatusCode(), ce.getMessage());
            throw ce;
        } catch (Exception ex) {
            log.error("getAllActive: unexpected error: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

}
