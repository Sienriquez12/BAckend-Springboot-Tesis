package com.especlub.match.controller.internal;

import com.especlub.match.docs.AdminRoleControllerDoc;
import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.models.UserRole;
import com.especlub.match.services.interfaces.AdminRoleService;
import com.especlub.match.shared.utils.RolePermissions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@PreAuthorize(RolePermissions.ADMIN_GENERAL)
public class RoleController implements AdminRoleControllerDoc {

    private final AdminRoleService adminRoleService;

    @GetMapping
    public ResponseEntity<JsonDtoResponse<List<UserRole>>> listAllActive() {
        List<UserRole> roles = adminRoleService.listAllActive();
        return JsonDtoResponse.ok("Roles retrieved", roles).toResponseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JsonDtoResponse<UserRole>> getById(@PathVariable Long id) {
        UserRole role = adminRoleService.getById(id);
        return JsonDtoResponse.ok("Role retrieved", role).toResponseEntity();
    }
}
