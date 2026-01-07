package com.especlub.match.controller.internal;

import com.especlub.match.dto.response.StudentMiAnswerDto;
import com.especlub.match.models.MultipleIntelligenceQuestion;
import com.especlub.match.services.interfaces.StudentMiService;
import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.dto.request.CreateStudentMiAnswersRequestDto;
import com.especlub.match.dto.response.StudentMiResultDto;
import com.especlub.match.services.impl.AuthServiceImpl;
import com.especlub.match.models.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students/mi")
@RequiredArgsConstructor
public class StudentMiController {

    private final StudentMiService studentMiService;
    private final AuthServiceImpl authService;

    @GetMapping("/questions")
    public ResponseEntity<JsonDtoResponse<List<MultipleIntelligenceQuestion>>> getAllQuestions() {
        List<MultipleIntelligenceQuestion> questions = studentMiService.getAllQuestions();
        return JsonDtoResponse.ok("Questions retrieved successfully", questions).toResponseEntity();
    }

    @GetMapping("/questions/{id}")
    public ResponseEntity<JsonDtoResponse<MultipleIntelligenceQuestion>> getQuestionById(@PathVariable Long id) {
        return studentMiService.getQuestionById(id)
                .map(question -> JsonDtoResponse.ok("Question retrieved successfully", question).toResponseEntity())
                .orElseGet(() -> JsonDtoResponse.<MultipleIntelligenceQuestion>notFound("Question not found").toResponseEntity());
    }

    @PostMapping("/answers")
    public ResponseEntity<JsonDtoResponse<StudentMiResultDto>> submitAnswers(HttpServletRequest request, @RequestBody CreateStudentMiAnswersRequestDto dto) {
        UserInfo currentUser = authService.validateUserJWT(request);
        Long userInfoId = currentUser.getId();
        StudentMiResultDto result = studentMiService.saveAnswersForUser(userInfoId, dto);
        return JsonDtoResponse.ok("MI Answers saved and results computed", result).toResponseEntity();
    }


    @GetMapping("/answers/me")
    public ResponseEntity<JsonDtoResponse<List<StudentMiAnswerDto>>> getMyAnswers(HttpServletRequest request) {
        UserInfo currentUser = authService.validateUserJWT(request);
        Long userInfoId = currentUser.getId();
        List<StudentMiAnswerDto> answers = studentMiService.getAnswersByUser(userInfoId);
        return JsonDtoResponse.ok("MI Answers retrieved", answers).toResponseEntity();
    }

}
