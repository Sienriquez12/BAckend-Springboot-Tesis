package com.especlub.match.controller.internal;

import com.especlub.match.models.SoftSkill;
import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.services.interfaces.SoftSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/soft-skills")
@RequiredArgsConstructor
public class SoftSkillController {

    private final SoftSkillService softSkillService;

    @GetMapping
    public ResponseEntity<JsonDtoResponse<List<SoftSkill>>> listAll() {
        List<SoftSkill> list = softSkillService.listAllActive();
        return JsonDtoResponse.ok("SoftSkills retrieved", list).toResponseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JsonDtoResponse<SoftSkill>> getById(@PathVariable Long id) {
        return softSkillService.findById(id)
                .map(s -> JsonDtoResponse.ok("SoftSkill retrieved", s).toResponseEntity())
                .orElseGet(() -> JsonDtoResponse.<SoftSkill>notFound("SoftSkill not found").toResponseEntity());
    }
}

