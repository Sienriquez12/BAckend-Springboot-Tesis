package com.especlub.match.controller.internal;

import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.models.ClubProfile;
import com.especlub.match.repositories.ClubProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/club-profiles")
@RequiredArgsConstructor
public class ClubProfileController {

    private final ClubProfileRepository clubProfileRepository;

    /**
     * Lista todos los ClubProfile marcados como activos para recomendaciones
     */
    @GetMapping
    public ResponseEntity<JsonDtoResponse<List<ClubProfile>>> listActiveForRecommendation() {
        List<ClubProfile> profiles = clubProfileRepository.findAllByIsActiveForRecommendationTrue();
        return JsonDtoResponse.ok("Club profiles retrieved", profiles).toResponseEntity();
    }

    /**
     * Obtener un ClubProfile por su id (solo si el club asociado está activo)
     */
    @GetMapping("/{id}")
    public ResponseEntity<JsonDtoResponse<ClubProfile>> getById(@PathVariable Long id) {
        return clubProfileRepository.findByIdAndClub_RecordStatusTrue(id)
                .map(profile -> JsonDtoResponse.ok("Club profile retrieved", profile).toResponseEntity())
                .orElseGet(() -> JsonDtoResponse.ok("Club profile not found", (ClubProfile) null).toResponseEntity());
    }

    /**
     * Obtener un ClubProfile por el id del Club
     */
    @GetMapping("/by-club/{clubId}")
    public ResponseEntity<JsonDtoResponse<ClubProfile>> getByClubId(@PathVariable Long clubId) {
        return clubProfileRepository.findByClub_Id(clubId)
                .map(profile -> JsonDtoResponse.ok("Club profile retrieved", profile).toResponseEntity())
                .orElseGet(() -> JsonDtoResponse.ok("Club profile not found", (ClubProfile) null).toResponseEntity());
    }
}
