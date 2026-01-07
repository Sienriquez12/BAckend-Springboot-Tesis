package com.especlub.match.controller.admin;

import com.especlub.match.docs.AdminUserControllerDoc;
import com.especlub.match.dto.request.CreateUserRequestDto;
import com.especlub.match.dto.request.UpdateUserRequestDto;
import com.especlub.match.dto.request.UserAdminDto;
import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.services.interfaces.AdminUserService;
import com.especlub.match.shared.utils.RolePermissions;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
@PreAuthorize(RolePermissions.ADMIN_GENERAL)
public class AdminUserController implements AdminUserControllerDoc {

    private final AdminUserService adminUserService;

    @Override
    @GetMapping
    public ResponseEntity<JsonDtoResponse<List<UserAdminDto>>> listAll() {
        List<UserAdminDto> users = adminUserService.listAllActive();
        return JsonDtoResponse.ok("Users retrieved", users).toResponseEntity();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<JsonDtoResponse<UserAdminDto>> getById(@PathVariable Long id) {
        UserAdminDto dto = adminUserService.getById(id);
        return JsonDtoResponse.ok("User retrieved", dto).toResponseEntity();
    }

    @Override
    @PostMapping
    public ResponseEntity<JsonDtoResponse<UserAdminDto>> create(@Valid @RequestBody CreateUserRequestDto dto) {
        UserAdminDto created = adminUserService.create(dto);
        return JsonDtoResponse.ok("User created", created).toResponseEntity();
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<JsonDtoResponse<UserAdminDto>> update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequestDto dto) {
        UserAdminDto updated = adminUserService.update(id, dto);
        return JsonDtoResponse.ok("User updated", updated).toResponseEntity();
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<JsonDtoResponse<Boolean>> delete(@PathVariable Long id) {
        boolean result = adminUserService.delete(id);
        return JsonDtoResponse.ok("User deleted", result).toResponseEntity();
    }
}