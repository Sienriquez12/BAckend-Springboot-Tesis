package com.especlub.match.services.impl;

import com.especlub.match.dto.request.CreateStudentMiAnswersRequestDto;
import com.especlub.match.dto.response.StudentMiResultDto;
import com.especlub.match.dto.response.StudentMiAnswerDto;
import com.especlub.match.models.MultipleIntelligenceQuestion;
import com.especlub.match.models.Student;
import com.especlub.match.models.StudentMiAnswer;
import com.especlub.match.models.UserInfo;
import com.especlub.match.repositories.MultipleIntelligenceQuestionRepository;
import com.especlub.match.repositories.StudentMiAnswerRepository;
import com.especlub.match.repositories.StudentRepository;
import com.especlub.match.repositories.UserInfoRepository;
import com.especlub.match.services.interfaces.StudentMiService;
import com.especlub.match.shared.exceptions.CustomExceptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentMiServiceImpl implements StudentMiService {

    private final MultipleIntelligenceQuestionRepository questionRepository;
    private final StudentMiAnswerRepository answerRepository;
    private final StudentRepository studentRepository;
    private final UserInfoRepository userInfoRepository;

    @Override
    public List<MultipleIntelligenceQuestion> getAllQuestions() {
        return questionRepository.findAllByRecordStatusTrue();
    }

    @Override
    public Optional<MultipleIntelligenceQuestion> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    @Override
    @Transactional
    public StudentMiResultDto saveAnswersForUser(Long userInfoId, CreateStudentMiAnswersRequestDto dto) {
        if (userInfoId == null) throw new CustomExceptions("userInfoId es requerido", 400);
        UserInfo ui = userInfoRepository.findById(userInfoId).orElseThrow(() -> new CustomExceptions("Usuario no encontrado", 404));

        // ensure Student exists or create
        Student student = studentRepository.findByUserInfo_IdAndRecordStatusTrue(userInfoId)
                .orElseGet(() -> {
                    Student s = Student.builder().userInfo(ui).recordStatus(true).build();
                    Student saved = studentRepository.save(s);
                    log.debug("Created student for userInfoId={}", userInfoId);
                    return saved;
                });

        if (dto == null || dto.getAnswers() == null || dto.getAnswers().isEmpty()) {
            throw new CustomExceptions("answers es requerido", 400);
        }

        List<Long> qids = dto.getAnswers().stream().map(a -> a.getQuestionId()).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<MultipleIntelligenceQuestion> questions = questionRepository.findAllById(qids);
        Map<Long, MultipleIntelligenceQuestion> qmap = questions.stream().collect(Collectors.toMap(MultipleIntelligenceQuestion::getId, q -> q));

        // validate each answer
        for (var a : dto.getAnswers()) {
            if (a.getQuestionId() == null) throw new CustomExceptions("questionId es requerido", 400);
            if (!qmap.containsKey(a.getQuestionId())) throw new CustomExceptions("Question not found id=" + a.getQuestionId(), 404);
            if (a.getScore() == null || a.getScore() < 1 || a.getScore() > 5) throw new CustomExceptions("Score must be between 1 and 5", 400);
        }

        // persist answers
        List<StudentMiAnswer> toSave = new ArrayList<>();
        for (var a : dto.getAnswers()) {
            MultipleIntelligenceQuestion q = qmap.get(a.getQuestionId());
            StudentMiAnswer ans = StudentMiAnswer.builder()
                    .student(student)
                    .question(q)
                    .score(a.getScore())
                    .recordStatus(true)
                    .createdAt(LocalDateTime.now())
                    .build();
            toSave.add(ans);
        }
        answerRepository.saveAll(toSave);
        log.debug("Saved {} MI answers for studentId={}", toSave.size(), student.getId());

        // compute totals per IntelligenceType
        Map<Long, Integer> totals = new LinkedHashMap<>();
        Map<Long, Integer> counts = new LinkedHashMap<>();
        for (StudentMiAnswer a : toSave) {
            if (a.getQuestion() == null || a.getQuestion().getIntelligenceType() == null) continue;
            Long typeId = a.getQuestion().getIntelligenceType().getId();
            totals.put(typeId, totals.getOrDefault(typeId, 0) + (a.getScore() == null ? 0 : a.getScore()));
            counts.put(typeId, counts.getOrDefault(typeId, 0) + 1);
        }

        // build DTO list
        List<StudentMiResultDto.IntelligenceScore> scoreList = new ArrayList<>();
        for (var entry : totals.entrySet()) {
            Long typeId = entry.getKey();
            Integer total = entry.getValue();
            Integer cnt = counts.getOrDefault(typeId, 1);
            double normalized = cnt == 0 ? 0.0 : (double) total / (cnt * 5.0); // normalize 0..1 where 5 is max per question
            // attempt to get code/name from any question
            MultipleIntelligenceQuestion anyQ = questions.stream().filter(q -> q.getIntelligenceType() != null && q.getIntelligenceType().getId().equals(typeId)).findFirst().orElse(null);
            String code = anyQ != null && anyQ.getIntelligenceType() != null ? anyQ.getIntelligenceType().getCode() : null;
            String name = anyQ != null && anyQ.getIntelligenceType() != null ? anyQ.getIntelligenceType().getName() : null;
            scoreList.add(StudentMiResultDto.IntelligenceScore.builder()
                    .intelligenceTypeId(typeId)
                    .code(code)
                    .name(name)
                    .totalScore(total)
                    .normalized(Math.round(normalized * 100.0) / 100.0)
                    .build());
        }

        // sort desc by normalized score
        scoreList.sort((a,b) -> Double.compare(b.getNormalized(), a.getNormalized()));

        StudentMiResultDto result = StudentMiResultDto.builder()
                .studentId(student.getId())
                .scores(scoreList)
                .build();

        return result;
    }

    @Override
    public List<StudentMiAnswerDto> getAnswersByUser(Long userInfoId) {
        if (userInfoId == null) throw new CustomExceptions("userInfoId es requerido", 400);
        // find student for this user
        Student student = studentRepository.findByUserInfo_IdAndRecordStatusTrue(userInfoId)
                .orElseThrow(() -> new CustomExceptions("Student not found for userInfoId=" + userInfoId, 404));

        List<StudentMiAnswer> answers = answerRepository.findAllByStudent_Id(student.getId());

        // Map to DTO with question text; handle lazy proxy by accessing question.getText()
        // Prefer repository method that fetches question to avoid lazy issues
        List<StudentMiAnswer> answersWithQ = answerRepository.findAllByStudentIdWithQuestion(student.getId());
        List<StudentMiAnswerDto> dtos = answersWithQ.stream().map(a -> {
            String qtext = a.getQuestion() != null ? a.getQuestion().getText() : "(pregunta no disponible)";
            return new StudentMiAnswerDto(qtext, a.getScore());
        }).collect(Collectors.toList());

        return dtos;
    }
}