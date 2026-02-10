package com.especlub.match.controller.internal;

import com.especlub.match.docs.AdminRoleControllerDoc;
import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.models.UserRole;
import com.especlub.match.repositories.UserRoleRepository;
import com.especlub.match.services.interfaces.AdminRoleService;
import com.especlub.match.shared.utils.RolePermissions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@PreAuthorize(RolePermissions.ADMIN_GENERAL)
public class RoleController implements AdminRoleControllerDoc {

    private final AdminRoleService adminRoleService;
    private final UserRoleRepository userRoleRepository;

    @GetMapping
    public ResponseEntity<JsonDtoResponse<List<UserRole>>> listAllActive() {
        Integer callerHierarchy = getCallerMinHierarchy();
        List<UserRole> roles = adminRoleService.listAllByMinHierarchy(callerHierarchy);
        return JsonDtoResponse.ok("Roles retrieved", roles).toResponseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JsonDtoResponse<UserRole>> getById(@PathVariable Long id) {
        Integer callerHierarchy = getCallerMinHierarchy();
        UserRole role = adminRoleService.getById(id);
        if (callerHierarchy != null && role.getHierarchy() < callerHierarchy) {
            // role hierarchy is more privileged (smaller number) than caller -> deny
            @SuppressWarnings("unchecked")
            ResponseEntity<JsonDtoResponse<UserRole>> forbidden = (ResponseEntity) JsonDtoResponse.forbidden("No tiene permiso para ver este rol").toResponseEntity();
            return forbidden;
        }
        return JsonDtoResponse.ok("Role retrieved", role).toResponseEntity();
    }

    // helper: compute the caller's most privileged role hierarchy (the minimum numeric hierarchy among their roles)
    private Integer getCallerMinHierarchy() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities() == null) return null;
        // authorities use role names like ROLE_ADMIN; map them to UserRole to get hierarchy
        return auth.getAuthorities().stream()
                .map(granted -> granted.getAuthority())
                .map(name -> userRoleRepository.findByNameAndRecordStatusTrue(name))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(UserRole::getHierarchy)
                .min(Integer::compareTo)
                .orElse(null);
    }
}
