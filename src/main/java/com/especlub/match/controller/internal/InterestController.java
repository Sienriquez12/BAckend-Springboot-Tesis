package com.especlub.match.controller.internal;

import com.especlub.match.models.Interest;
import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.services.interfaces.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/interests")
@RequiredArgsConstructor
public class InterestController {

    private final InterestService interestService;

    @GetMapping
    public ResponseEntity<JsonDtoResponse<List<Interest>>> listAll() {
        List<Interest> list = interestService.listAllActive();
        return JsonDtoResponse.ok("Interests retrieved", list).toResponseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JsonDtoResponse<Interest>> getById(@PathVariable Long id) {
        return interestService.findById(id)
                .map(i -> JsonDtoResponse.ok("Interest retrieved", i).toResponseEntity())
                .orElseGet(() -> JsonDtoResponse.<Interest>notFound("Interest not found").toResponseEntity());
    }
}
