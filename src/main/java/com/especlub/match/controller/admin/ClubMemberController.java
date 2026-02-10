package com.especlub.match.controller.admin;

import com.especlub.match.dto.response.ClubMemberAdminDto;
import com.especlub.match.dto.response.ClubSummaryDto;
import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.models.ClubMember;
import com.especlub.match.repositories.ClubMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/club-members")
@RequiredArgsConstructor
public class ClubMemberController {

    private final ClubMemberRepository clubMemberRepository;

    @GetMapping
    public ResponseEntity<JsonDtoResponse<List<ClubMemberAdminDto>>> listAllActive() {
        List<ClubMember> members = clubMemberRepository.findAllActiveWithStudentAndUserInfo();
        List<ClubMemberAdminDto> dto = members.stream().map(this::toDto).toList();
        return JsonDtoResponse.ok("Club members retrieved", dto).toResponseEntity();
    }

    @GetMapping("/club/{clubId}")
    public ResponseEntity<JsonDtoResponse<List<ClubMemberAdminDto>>> listByClub(@PathVariable Long clubId) {
        List<ClubMember> members = clubMemberRepository.findAllByClubIdAndRecordStatusTrue(clubId);
        List<ClubMemberAdminDto> dto = members.stream().map(this::toDto).toList();
        return JsonDtoResponse.ok("Club members for club retrieved", dto).toResponseEntity();
    }

    @GetMapping("/{membershipId}")
    public ResponseEntity<JsonDtoResponse<ClubMemberAdminDto>> getById(@PathVariable Long membershipId) {
        Optional<ClubMember> maybe = clubMemberRepository.findById(membershipId);
        return maybe.map(cm -> JsonDtoResponse.ok("Club member retrieved", toDto(cm)).toResponseEntity())
                .orElseGet(() -> JsonDtoResponse.<ClubMemberAdminDto>notFound("Club member not found").toResponseEntity());
    }

    private ClubMemberAdminDto toDto(ClubMember cm) {
        if (cm == null) return null;
        var ui = cm.getUserInfo();
        Long studentId = cm.getStudent() != null ? cm.getStudent().getId() : null;
        Long userInfoId = ui != null ? ui.getId() : null;
        String email = ui != null ? ui.getEmail() : null;
        String first = ui != null && ui.getNames() != null ? ui.getNames().trim() : "";
        String last = ui != null && ui.getSurnames() != null ? ui.getSurnames().trim() : "";
        String full = (first + " " + last).trim();
        if (full.isEmpty() && ui != null) {
            if (ui.getUsername() != null && !ui.getUsername().isBlank()) full = ui.getUsername();
            else if (email != null) full = email;
        }

        List<ClubSummaryDto> clubs = null;
        if (studentId != null) {
            // fetch memberships for the student and map to distinct club summaries
            var memberships = clubMemberRepository.findAllByStudentIdAndRecordStatusTrue(studentId);
            clubs = memberships.stream()
                    .map(m -> {
                        var c = m.getClub();
                        return ClubSummaryDto.builder()
                                .id(c != null ? c.getId() : null)
                                .name(c != null ? c.getName() : null)
                                .recordStatus(c != null ? c.getRecordStatus() : null)
                                .build();
                    })
                    .distinct()
                    .collect(Collectors.toList());
        }

        return ClubMemberAdminDto.builder()
                .membershipId(cm.getId())
                .studentId(studentId)
                .userInfoId(userInfoId)
                .email(email)
                .fullName(full)
                .recordStatus(cm.getRecordStatus())
                .joinedAt(cm.getCreatedAt())
                .clubs(clubs)
                .isPresident(Boolean.TRUE.equals(cm.getIsPresident()))
                .build();
    }
}
