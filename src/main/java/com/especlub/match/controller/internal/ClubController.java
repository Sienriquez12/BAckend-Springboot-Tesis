package com.especlub.match.controller.internal;

import com.especlub.match.docs.ClubControllerDoc;
import com.especlub.match.dto.response.ClubAdminDto;
import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.services.AdminClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clubs")
@RequiredArgsConstructor
public class ClubController implements ClubControllerDoc {
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
}
