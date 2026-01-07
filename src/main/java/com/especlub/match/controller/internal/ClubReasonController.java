package com.especlub.match.controller.internal;

import com.especlub.match.models.ClubReason;
import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.services.interfaces.ClubReasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/club-reasons")
@RequiredArgsConstructor
public class ClubReasonController {

    private final ClubReasonService clubReasonService;

    @GetMapping
    public ResponseEntity<JsonDtoResponse<List<ClubReason>>> listAll() {
        List<ClubReason> list = clubReasonService.listAllActive();
        return JsonDtoResponse.ok("Club reasons retrieved", list).toResponseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JsonDtoResponse<ClubReason>> getById(@PathVariable Long id) {
        return clubReasonService.findById(id)
                .map(r -> JsonDtoResponse.ok("Club reason retrieved", r).toResponseEntity())
                .orElseGet(() -> JsonDtoResponse.<ClubReason>notFound("Club reason not found").toResponseEntity());
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<JsonDtoResponse<ClubReason>> getByName(@PathVariable String name) {
        return clubReasonService.findByName(name)
                .map(r -> JsonDtoResponse.ok("Club reason retrieved", r).toResponseEntity())
                .orElseGet(() -> JsonDtoResponse.<ClubReason>notFound("Club reason not found").toResponseEntity());
    }
}
