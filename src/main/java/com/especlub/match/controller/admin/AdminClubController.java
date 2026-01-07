package com.especlub.match.controller.admin;

import com.especlub.match.docs.AdminClubControllerDoc;
import com.especlub.match.dto.request.CreateClubRequestDto;
import com.especlub.match.dto.request.UpdateClubRequestDto;
import com.especlub.match.dto.response.ClubAdminDto;
import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.services.AdminClubService;
import com.especlub.match.shared.utils.RolePermissions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/clubs")
@RequiredArgsConstructor
@PreAuthorize(RolePermissions.ADMIN_CLUBS)
public class AdminClubController implements AdminClubControllerDoc {

    private final AdminClubService adminClubService;

    @GetMapping
    public ResponseEntity<JsonDtoResponse<List<ClubAdminDto>>> listAll() {
        List<ClubAdminDto> list = adminClubService.listAll();
        return JsonDtoResponse.ok("Clubs obtenidos", list).toResponseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JsonDtoResponse<ClubAdminDto>> getById(@PathVariable Long id) {
        ClubAdminDto dto = adminClubService.getById(id);
        return JsonDtoResponse.ok("Club obtenido", dto).toResponseEntity();
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<JsonDtoResponse<List<com.especlub.match.dto.response.ClubMemberAdminDto>>> listMembers(@PathVariable Long id) {
        List<com.especlub.match.dto.response.ClubMemberAdminDto> members = adminClubService.listMembers(id);
        return JsonDtoResponse.ok("Miembros del club obtenidos", members).toResponseEntity();
    }

    @PostMapping
    public ResponseEntity<JsonDtoResponse<ClubAdminDto>> create(@RequestBody CreateClubRequestDto dto) {
        ClubAdminDto created = adminClubService.create(dto);
        return JsonDtoResponse.created("Club creado", created).toResponseEntity();
    }

    @PutMapping("/{id}")
    public ResponseEntity<JsonDtoResponse<ClubAdminDto>> update(@PathVariable Long id, @RequestBody UpdateClubRequestDto dto) {
        ClubAdminDto updated = adminClubService.update(id, dto);
        return JsonDtoResponse.ok("Club actualizado", updated).toResponseEntity();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<JsonDtoResponse<Boolean>> delete(@PathVariable Long id) {
        boolean deleted = adminClubService.delete(id);
        return JsonDtoResponse.ok("Club eliminado", deleted).toResponseEntity();
    }
}
