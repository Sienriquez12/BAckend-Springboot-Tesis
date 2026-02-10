package com.especlub.match.controller.internal;

import com.especlub.match.docs.StudentClubControllerDoc;
import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.dto.response.ClubMemberAdminDto;
import com.especlub.match.dto.response.ClubAdminDto;
import com.especlub.match.dto.response.ClubMembersByClubDto;
import com.especlub.match.models.UserInfo;
import com.especlub.match.services.impl.AuthServiceImpl;
import com.especlub.match.services.interfaces.StudentClubService;
import com.especlub.match.services.AdminClubService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/clubs")
@RequiredArgsConstructor
@Slf4j
public class StudentClubController implements StudentClubControllerDoc {

    private final StudentClubService studentClubService;

    private final AuthServiceImpl authService;

    private final AdminClubService adminClubService;

    @GetMapping("/my")
    public ResponseEntity<JsonDtoResponse<List<ClubAdminDto>>> myClubs(HttpServletRequest request) {
        UserInfo currentUser = authService.validateUserJWT(request);
        Long userInfoId = currentUser.getId();
        log.debug("myClubs: userInfoId={}", userInfoId);
        List<ClubAdminDto> clubs = studentClubService.findClubsByUserInfoId(userInfoId);
        return JsonDtoResponse.ok("Clubs del usuario obtenidos", clubs).toResponseEntity();
    }


    // delete row the menber clubs
    @PostMapping("/{clubId}/enroll")
    public ResponseEntity<JsonDtoResponse<String>> enroll(@PathVariable Long clubId, HttpServletRequest request) {
        UserInfo currentUser = authService.validateUserJWT(request);
        Long studentId = currentUser.getId(); // adjust if UserInfo uses a different getter
        log.debug("enroll: userInfoId={} clubId={}", studentId, clubId);
        String whatsappLink = studentClubService.enrollStudent(studentId, clubId);
        log.debug("enroll: returned whatsappLink={}", whatsappLink);
        return JsonDtoResponse.ok("Inscripción realizada", whatsappLink).toResponseEntity();
    }

    @PostMapping("/{clubId}/leave")
    public ResponseEntity<JsonDtoResponse<Void>> leave(@PathVariable Long clubId, HttpServletRequest request) {
        UserInfo currentUser = authService.validateUserJWT(request);
        Long studentId = currentUser.getId(); // adjust if UserInfo uses a different getter
        log.debug("leave: userInfoId={} clubId={}", studentId, clubId);
        studentClubService.leaveClub(studentId, clubId);
        log.debug("leave: completed for userInfoId={} clubId={}", studentId, clubId);
        return JsonDtoResponse.<Void>ok("Salida del club realizada", null).toResponseEntity();
    }

    @GetMapping("/mine/members")
    public ResponseEntity<JsonDtoResponse<List<ClubMembersByClubDto>>> getMyClubMembers(HttpServletRequest request) {
        UserInfo currentUser = authService.validateUserJWT(request);
        Long userInfoId = currentUser.getId();
        log.debug("getMyClubMembers: userInfoId={}", userInfoId);

        List<ClubAdminDto> clubs = studentClubService.findClubsByUserInfoId(userInfoId);

        if (clubs == null || clubs.isEmpty()) {
            log.debug("getMyClubMembers: no clubs for userInfoId={}", userInfoId);
            return JsonDtoResponse.ok("Miembros de mis clubs obtenidos", List.<ClubMembersByClubDto>of()).toResponseEntity();
        }

        List<ClubMembersByClubDto> result = clubs.stream()
                .filter(c -> c != null && c.getId() != null)
                .map(c -> ClubMembersByClubDto.builder()
                        .clubId(c.getId())
                        .club(c)
                        .members(adminClubService.listMembers(c.getId()))
                        .president(adminClubService.getPresidentByClubId(c.getId()))
                        .build())
                .toList();

        for (ClubMembersByClubDto item : result) {
            if (item.getClubId() == null) {
                log.warn("getMyClubMembers: built item has null clubId, club dto={}", item.getClub());
            } else {
                log.debug("getMyClubMembers: built item clubId={} membersCount={}", item.getClubId(), item.getMembers() == null ? 0 : item.getMembers().size());
            }
        }
        log.debug("getMyClubMembers: final result size={}", result.size());

        return JsonDtoResponse.ok("Miembros de mis clubs obtenidos", result).toResponseEntity();
    }

    @GetMapping("/{clubId}/members")
    public ResponseEntity<JsonDtoResponse<List<ClubMemberAdminDto>>> getClubMembers(@PathVariable Long clubId, HttpServletRequest request) {
        UserInfo currentUser = authService.validateUserJWT(request);
        Long userInfoId = currentUser.getId();
        log.debug("getClubMembers: userInfoId={} clubId={}", userInfoId, clubId);

        List<ClubAdminDto> clubs = studentClubService.findClubsByUserInfoId(userInfoId);
        boolean belongs = clubs.stream().anyMatch(c -> c != null && c.getId() != null && c.getId().equals(clubId));
        if (!belongs) {
            throw new com.especlub.match.shared.exceptions.CustomExceptions("No autorizado para ver miembros de este club", 403);
        }

        List<ClubMemberAdminDto> members = adminClubService.listMembers(clubId);
        return JsonDtoResponse.ok("Miembros del club obtenidos", members).toResponseEntity();
    }
}