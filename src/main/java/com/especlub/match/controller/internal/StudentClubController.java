package com.especlub.match.controller.internal;

import com.especlub.match.docs.StudentClubControllerDoc;
import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.models.UserInfo;
import com.especlub.match.services.impl.AuthServiceImpl;
import com.especlub.match.services.interfaces.StudentClubService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clubs")
@RequiredArgsConstructor
@Slf4j
public class StudentClubController implements StudentClubControllerDoc {

    private final StudentClubService studentClubService;

    private final AuthServiceImpl authService;

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
}