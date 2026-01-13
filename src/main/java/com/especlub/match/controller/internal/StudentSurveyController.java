package com.especlub.match.controller.internal;

import com.especlub.match.dto.request.CreateSurveyRequestDto;
import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.dto.response.RecommendationListDto;
import com.especlub.match.dto.response.StudentSurveyResponseDto;
import com.especlub.match.models.StudentSurvey;
import com.especlub.match.models.UserInfo;
import com.especlub.match.models.Student;
import com.especlub.match.services.StudentSurveyService;
import com.especlub.match.docs.StudentSurveyControllerDoc;
import com.especlub.match.services.impl.AuthServiceImpl;
import com.especlub.match.repositories.StudentRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentSurveyController implements StudentSurveyControllerDoc {

    private final StudentSurveyService studentSurveyService;
    private final AuthServiceImpl authService;
    private final StudentRepository studentRepository;


    @PostMapping("/survey")
    public ResponseEntity<JsonDtoResponse<StudentSurveyResponseDto>> createSurvey(
            HttpServletRequest request,
            @Valid @RequestBody CreateSurveyRequestDto dto) {

        UserInfo currentUser = authService.validateUserJWT(request);
        Long userInfoId = currentUser.getId();
        log.info("El usuario es: {}", currentUser.getUsername());

        // buscar el Student asociado al UserInfo (usuario distinto de student)
        Student student = studentRepository.findByUserInfo_IdAndRecordStatusTrue(userInfoId)
                .orElseThrow(() -> new RuntimeException("Student not found for userInfoId=" + userInfoId));
        Long studentId = student.getId();

        StudentSurvey saved = studentSurveyService.saveSurvey(studentId, dto);
        StudentSurveyResponseDto resp = studentSurveyService.toResponseDto(saved);

        return JsonDtoResponse.created("Survey saved", resp).toResponseEntity();
    }

    @PostMapping("/survey/recommendation")
    public ResponseEntity<JsonDtoResponse<RecommendationListDto>> generateRecommendation(HttpServletRequest request) {
        UserInfo currentUser = authService.validateUserJWT(request);
        Long userInfoId = currentUser.getId();
        Student student = studentRepository.findByUserInfo_IdAndRecordStatusTrue(userInfoId)
                .orElseThrow(() -> new RuntimeException("Student not found for userInfoId=" + userInfoId));
        Long studentId = student.getId();

        studentSurveyService.generateRecommendation(studentId);
        RecommendationListDto recommendations = studentSurveyService.recommendClubs(studentId);
        return JsonDtoResponse.ok("Recommendations generated", recommendations).toResponseEntity();
    }
}