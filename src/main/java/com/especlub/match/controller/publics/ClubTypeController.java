package com.especlub.match.controller.publics;

import com.especlub.match.dto.response.ClubTypeDto;
import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.models.ClubType;
import com.especlub.match.services.impl.ClubTypeServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/club-types")
@RequiredArgsConstructor
@Slf4j
public class ClubTypeController {

    private final ClubTypeServiceImpl clubTypeService;

    @GetMapping
    public ResponseEntity<JsonDtoResponse<List<ClubTypeDto>>> listAll() {
        log.debug("ClubTypeController.listAll: retrieving all active club types");
        List<ClubType> types = clubTypeService.listAllActive();
        List<ClubTypeDto> dtos = types.stream().map(clubTypeService::toDto).collect(Collectors.toList());
        return JsonDtoResponse.ok("Club types obtenidos", dtos).toResponseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JsonDtoResponse<ClubTypeDto>> getById(@PathVariable Long id) {
        log.debug("ClubTypeController.getById: id={}", id);
        ClubType ct = clubTypeService.findById(id).orElseThrow(() -> new com.especlub.match.shared.exceptions.CustomExceptions("Tipo de club no encontrado", 404));
        ClubTypeDto dto = clubTypeService.toDto(ct);
        return JsonDtoResponse.ok("Tipo de club obtenido", dto).toResponseEntity();
    }
}
