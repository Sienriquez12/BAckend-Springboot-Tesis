package com.especlub.match.services.impl;

import com.especlub.match.dto.request.CreateSurveyRequestDto;
import com.especlub.match.dto.response.StudentSurveyResponseDto;
import com.especlub.match.models.Club;
import com.especlub.match.models.Student;
import com.especlub.match.models.StudentSurvey;
import com.especlub.match.repositories.ClubReasonRepository;
import com.especlub.match.repositories.InterestRepository;
import com.especlub.match.repositories.SoftSkillRepository;
import com.especlub.match.repositories.StudentRepository;
import com.especlub.match.repositories.StudentSurveyRepository;
import com.especlub.match.repositories.ClubRepository;
import com.especlub.match.services.StudentSurveyService;
import com.especlub.match.shared.exceptions.CustomExceptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import com.especlub.match.repositories.LlmInteractionRepository;
import com.especlub.match.repositories.UserInfoRepository;
import com.especlub.match.repositories.StudentMiAnswerRepository;
import com.especlub.match.models.StudentMiAnswer;
import com.especlub.match.repositories.ClubTypeRepository;
import com.especlub.match.models.ClubType;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentSurveyServiceImpl implements StudentSurveyService {

    private final StudentRepository studentRepository;
    private final StudentSurveyRepository studentSurveyRepository;
    private final InterestRepository interestRepository;
    private final SoftSkillRepository softSkillRepository;
    private final ClubReasonRepository clubReasonRepository;
    private final ClubRepository clubRepository;
    private final ChatModel chatModel;
    private final LlmInteractionRepository llmInteractionRepository;
    private final UserInfoRepository userInfoRepository;
    private final com.especlub.match.repositories.StudentPreferenceRepository studentPreferenceRepository;
    private final StudentMiAnswerRepository studentMiAnswerRepository;
    private final ClubTypeRepository clubTypeRepository;

    @Override
    @Transactional
    public StudentSurvey saveSurvey(Long studentId, CreateSurveyRequestDto dto) {
        if (studentId == null) throw new CustomExceptions("studentId es requerido", 400);

        // Authorization: ensure authenticated user is the student or has admin authority
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new CustomExceptions("No autenticado", 401);
        }
        String username = auth.getName();
        Set<String> roles = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        Student student = studentRepository.findByIdAndRecordStatusTrue(studentId)
                .orElseThrow(() -> new CustomExceptions("Student not found or inactive", 404));

        String studentUsername = student.getUserInfo() != null ? student.getUserInfo().getUsername() : null;
        boolean isAdmin = roles.stream().anyMatch(r -> r.equals("ROLE_ADMIN") || r.equals("ROLE_SUPERADMIN") || r.equals("ROLE_PSYCHOLOGIST") || r.equals("ROLE_TEACHER"));
        if (!isAdmin && (studentUsername == null || !studentUsername.equals(username))) {
            throw new CustomExceptions("No autorizado para crear encuestas para este estudiante", 403);
        }

        // If a survey already exists for this student, overwrite it (keep only the latest)
        StudentSurvey survey;
        var existing = studentSurveyRepository.findByStudentId(studentId);
        if (existing.isPresent()) {
            survey = existing.get();
            log.debug("saveSurvey: overwriting existing survey id={} for studentId={}", survey.getId(), studentId);
            // reset fields that will be recalculated
            survey.setDerivedInterests(null);
            survey.setDerivedSoftSkills(null);
            survey.setDerivedClubReasons(null);
            survey.setCompletedAt(LocalDateTime.now());
            survey.setUpdatedAt(LocalDateTime.now());
            // bump numeric surveyVersion if possible
            String ver = survey.getSurveyVersion();
            try {
                int v = ver != null ? Integer.parseInt(ver) : 0;
                survey.setSurveyVersion(String.valueOf(v + 1));
            } catch (Exception e) {
                survey.setSurveyVersion("1");
            }
        } else {
            survey = StudentSurvey.builder()
                    .student(student)
                    .completedAt(LocalDateTime.now())
                    .createdAt(LocalDateTime.now())
                    .surveyVersion("1")
                    .build();
            log.debug("saveSurvey: creating new survey for studentId={}", studentId);
        }

        // Persistir survey (sin llamar al LLM). La recomendación se genera por separado

        // flag para saber si debemos persistir el Student con cambios en colecciones/campos
        boolean updated = false;
        // flag para saber si se actualizaron colecciones (interests/softSkills/reasons)
        boolean collectionsUpdated = false;

        // derived interests
        if (dto.getInterestIds() != null && !dto.getInterestIds().isEmpty()) {
            var list = interestRepository.findAllByIdInAndRecordStatusTrue(dto.getInterestIds());
            survey.setDerivedInterests(new HashSet<>(list));
            // also persist on Student (student_interests join table)
            student.setInterests(new HashSet<>(list));
            collectionsUpdated = true;
        }
        if (dto.getSoftSkillIds() != null && !dto.getSoftSkillIds().isEmpty()) {
            var list = softSkillRepository.findAllByIdInAndRecordStatusTrue(dto.getSoftSkillIds());
            survey.setDerivedSoftSkills(new HashSet<>(list));
            // also persist on Student (student_soft_skills join table)
            student.setSoftSkills(new HashSet<>(list));
            collectionsUpdated = true;
        }
        if (dto.getClubReasonIds() != null && !dto.getClubReasonIds().isEmpty()) {
            var list = clubReasonRepository.findAllByIdInAndRecordStatusTrue(dto.getClubReasonIds());
            survey.setDerivedClubReasons(new HashSet<>(list));
            // also persist on Student (student_preferred_reasons join table)
            student.setPreferredReasons(new HashSet<>(list));
            collectionsUpdated = true;
        }

        // If we updated collections on Student, save immediately so join tables are persisted
        if (collectionsUpdated) {
            studentRepository.saveAndFlush(student);
            log.debug("Persisted student collections for studentId={}", studentId);
            // ensure student is managed and fresh
            student = studentRepository.findByIdAndRecordStatusTrue(studentId).orElse(student);
        }


        // persist the survey (either newly created or updated)
        StudentSurvey saved = studentSurveyRepository.saveAndFlush(survey);
        // reload to ensure relationships are initialized (and join tables persisted)
        saved = studentSurveyRepository.findById(saved.getId()).orElse(saved);
        log.debug("Saved survey id={} derivedInterests={} derivedSoftSkills={} derivedClubReasons={}", saved.getId(),
                saved.getDerivedInterests() != null ? saved.getDerivedInterests().size() : 0,
                saved.getDerivedSoftSkills() != null ? saved.getDerivedSoftSkills().size() : 0,
                saved.getDerivedClubReasons() != null ? saved.getDerivedClubReasons().size() : 0);

        if (dto.getWeeklyAvailabilityHours() != null) {
            student.setWeeklyAvailabilityHours(dto.getWeeklyAvailabilityHours());
            updated = true;
        }
        if (dto.getMaxParallelClubs() != null) {
            student.setMaxParallelClubs(dto.getMaxParallelClubs());
            updated = true;
        }
        if (dto.getRecommendationOptIn() != null) {
            student.setRecommendationOptIn(dto.getRecommendationOptIn());
            updated = true;
        }
        if (dto.getSemesterNumber() != null) {
            student.setSemesterNumber(dto.getSemesterNumber());
            updated = true;
        }
        // if client sends preferredClubTypeId at root, resolve to ClubType name and persist on Student
        if (dto.getPreferredClubTypeId() != null) {
            ClubType ct = clubTypeRepository.findByIdAndRecordStatusTrue(dto.getPreferredClubTypeId()).orElse(null);
            if (ct != null) student.setPreferredClubType(ct.getName());
            else student.setPreferredClubType(dto.getPreferredClubTypeId().toString());
            updated = true;
        }
        if (dto.getIsOpenToNewExperiences() != null) {
            student.setIsOpenToNewExperiences(dto.getIsOpenToNewExperiences());
            updated = true;
        }

        if (updated) {
            studentRepository.save(student);
        }

        return saved;
    }

    @Override
    @Transactional
    public StudentSurvey generateRecommendation(Long studentId) {
        if (studentId == null) throw new CustomExceptions("studentId es requerido", 400);

        // Authorization: ensure authenticated user can request recommendation for this student
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new CustomExceptions("No autenticado", 401);
        }
        String username = auth.getName();
        Set<String> roles = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        StudentSurvey survey = studentSurveyRepository.findByStudentId(studentId)
                .orElseThrow(() -> new CustomExceptions("Survey not found for student", 404));

        Student student = survey.getStudent();
        String studentUsername = student.getUserInfo() != null ? student.getUserInfo().getUsername() : null;
        boolean isAdmin = roles.stream().anyMatch(r -> r.equals("ROLE_ADMIN") || r.equals("ROLE_SUPERADMIN") || r.equals("ROLE_PSYCHOLOGIST") || r.equals("ROLE_TEACHER"));
        if (!isAdmin && (studentUsername == null || !studentUsername.equals(username))) {
            throw new CustomExceptions("No autorizado para generar recomendaciones para este estudiante", 403);
        }
        // build prompt and call LLM, store prompt and response
        buildAndCallLlm(survey, student);
        return studentSurveyRepository.save(survey);
    }

    @Override
    @Transactional(readOnly = true)
    public com.especlub.match.dto.response.RecommendationListDto recommendClubs(Long studentId) {
        // basic scoring heuristic: match number of overlapping interests + soft skills; availability penalty
        StudentSurvey survey = studentSurveyRepository.findByStudentId(studentId)
                .orElseThrow(() -> new CustomExceptions("Survey not found for student", 404));
        Student student = survey.getStudent();

        List<Club> clubs = clubRepository.findAllByRecordStatusTrue();

        Set<String> studentInterests = survey.getDerivedInterests() != null ? survey.getDerivedInterests().stream().map(i -> i.getName().toLowerCase()).collect(Collectors.toSet()) : Set.of();
        Set<String> studentSoftSkills = survey.getDerivedSoftSkills() != null ? survey.getDerivedSoftSkills().stream().map(s -> s.getName().toLowerCase()).collect(Collectors.toSet()) : Set.of();

        List<com.especlub.match.dto.response.RecommendationDto> recs = clubs.stream().map(c -> {
            double score = 0.0;
            // interest overlap
            if (c.getInterests() != null) {
                long overlap = c.getInterests().stream().map(ci -> ci.getName().toLowerCase()).filter(studentInterests::contains).count();
                score += overlap * 1.0;
            }
            // soft skill overlap
            if (c.getDesiredSoftSkills() != null) {
                long overlap = c.getDesiredSoftSkills().stream().map(cs -> cs.getName().toLowerCase()).filter(studentSoftSkills::contains).count();
                score += overlap * 0.7;
            }
            // semester compatibility bonus
            if (c.getProfile() != null && student.getSemesterNumber() != null) {
                Integer minS = c.getProfile().getMinSemester();
                Integer maxS = c.getProfile().getMaxSemester();
                if (minS != null && student.getSemesterNumber() < minS) score -= 0.5;
                if (maxS != null && student.getSemesterNumber() > maxS) score -= 0.5;
            }
            // availability penalty if expected hours > student weeklyAvailabilityHours
            if (c.getProfile() != null && student.getWeeklyAvailabilityHours() != null && c.getProfile().getExpectedWeeklyCommitmentHours() != null) {
                int expected = c.getProfile().getExpectedWeeklyCommitmentHours();
                if (expected > student.getWeeklyAvailabilityHours()) score -= 0.3;
            }

            // clamp
            if (score < 0) score = 0.0;
            // ensure maximum 1.0 as requested
            if (score > 1.0) score = 1.0;

            String reason = String.format("score=%.2f (interests+softskills overlap and availability/semester checks)", score);

            return com.especlub.match.dto.response.RecommendationDto.builder()
                    .clubId(c.getId())
                    .clubName(c.getName())
                    .score(Math.round(score * 100.0) / 100.0)
                    .reason(reason)
                    .build();
        }).sorted((a,b) -> Double.compare(b.getScore(), a.getScore())).toList();

        return com.especlub.match.dto.response.RecommendationListDto.builder()
                .studentId(studentId)
                .recommendations(recs)
                .build();
    }

    private void buildAndCallLlm(StudentSurvey survey, Student student) {
        try {
            List<Club> clubs = clubRepository.findAllByRecordStatusTrue();

            StringBuilder sb = new StringBuilder();
            sb.append("Por favor, based on the following student profile and list of clubs, recommend which clubs the student should join.\n");
            sb.append("Question: ¿Qué clubes me recomiendas según los clubs que hay configurados en la BD?\n\n");

            sb.append("Student:\n");
            sb.append("- id: ").append(student.getId()).append("\n");
            if (student.getUserInfo() != null) {
                sb.append("- username: ").append(student.getUserInfo().getUsername()).append("\n");
                sb.append("- names: ").append(student.getUserInfo().getNames()).append(" ").append(student.getUserInfo().getSurnames()).append("\n");
            }
            sb.append("- career: ").append(student.getCareer() != null ? student.getCareer().getName() : "(no especificada)").append("\n");
            sb.append("- semester: ").append(student.getSemesterNumber()).append("\n");
            sb.append("- weeklyAvailabilityHours: ").append(student.getWeeklyAvailabilityHours()).append("\n");
            sb.append("- maxParallelClubs: ").append(student.getMaxParallelClubs()).append("\n");
            sb.append("- preferredClubType: ").append(student.getPreferredClubType()).append("\n");
            sb.append("- shortTermGoal: ").append(student.getShortTermGoal()).append("\n");
            sb.append("- longTermGoal: ").append(student.getLongTermGoal()).append("\n\n");

            // attach Multiple Intelligence (MI) answers (questions + user's scores)
            try {
                List<StudentMiAnswer> miAnswers = studentMiAnswerRepository.findAllByStudentIdWithQuestion(student.getId());
                if (miAnswers != null && !miAnswers.isEmpty()) {
                    sb.append("Multiple Intelligence Answers:\n");
                    StringBuilder midump = new StringBuilder();
                    for (StudentMiAnswer a : miAnswers) {
                        String qtext = a.getQuestion() != null ? a.getQuestion().getText() : "(pregunta no disponible)";
                        Integer score = a.getScore();
                        sb.append("- questionId: ").append(a.getQuestion() != null ? a.getQuestion().getId() : null).append("\n");
                        sb.append("  text: ").append(qtext.replace("\n", " ")).append("\n");
                        sb.append("  score: ").append(score).append("\n\n");
                        midump.append("Q(id=").append(a.getQuestion() != null ? a.getQuestion().getId() : null)
                                .append("): ").append(qtext.replace("\n", " "))
                                .append(" => score=").append(score).append("\n");
                    }
                    // Log the MI answers that are sent to Gemini
                    log.info("--- MI answers sent to Gemini ---\n{}--- End MI ---", midump.toString());
                }
            } catch (Exception e) {
                log.warn("Could not load MI answers for prompt: {}", e.getMessage());
            }

            sb.append("Clubs:\n");
            for (Club c : clubs) {
                sb.append("- id: ").append(c.getId()).append("\n");
                sb.append("  name: ").append(c.getName()).append("\n");
                sb.append("  description: ").append(c.getDescription() != null ? c.getDescription().replace("\n", " ") : "").append("\n");
                if (c.getProfile() != null) {
                    sb.append("  type: ").append(c.getProfile().getClubType()).append("\n");
                    sb.append("  targetCareers: ").append(c.getProfile().getTargetCareers()).append("\n");
                    sb.append("  minSemester: ").append(c.getProfile().getMinSemester()).append("\n");
                    sb.append("  maxSemester: ").append(c.getProfile().getMaxSemester()).append("\n");
                    sb.append("  expectedWeeklyCommitmentHours: ").append(c.getProfile().getExpectedWeeklyCommitmentHours()).append("\n");
                    sb.append("  acceptsBeginners: ").append(c.getProfile().getAcceptsBeginners()).append("\n");
                    sb.append("  isActiveForRecommendation: ").append(c.getProfile().getIsActiveForRecommendation()).append("\n");
                }

                if (c.getInterests() != null && !c.getInterests().isEmpty()) {
                    sb.append("  interests: ")
                            .append(c.getInterests().stream().map(com.especlub.match.models.Interest::getName).collect(Collectors.joining(", ")))
                            .append("\n");
                }
                if (c.getDesiredSoftSkills() != null && !c.getDesiredSoftSkills().isEmpty()) {
                    sb.append("  desiredSoftSkills: ")
                            .append(c.getDesiredSoftSkills().stream().map(com.especlub.match.models.SoftSkill::getName).collect(Collectors.joining(", ")))
                            .append("\n");
                }

                sb.append("\n");
            }

            String prompt = sb.toString();
            log.info("--- Prompt to Gemini ---\n{}\n--- End Prompt ---", prompt);

            String response = callChatModel(prompt);
            log.info("--- Gemini response ---\n{}\n--- End response ---", response);

            survey.setRawAnswersJson(prompt);
            survey.setLlmResponse(response);

            try {
                String username = SecurityContextHolder.getContext().getAuthentication() != null
                        ? SecurityContextHolder.getContext().getAuthentication().getName() : null;
                com.especlub.match.models.UserInfo user = null;
                if (username != null) user = userInfoRepository.findByUsernameAndRecordStatusTrue(username);

                com.especlub.match.models.LlmInteraction interaction = com.especlub.match.models.LlmInteraction.builder()
                        .user(user)
                        .studentSurvey(survey)
                        .model("gemini")
                        .prompt(prompt)
                        .response(response)
                        .build();
                llmInteractionRepository.save(interaction);
            } catch (Exception e) {
                log.warn("Could not persist LLM interaction: {}", e.getMessage());
            }
        } catch (Exception ex) {
            throw new CustomExceptions("Error generating rawAnswersJson: " + ex.getMessage(), 500);
        }
    }

    private String callChatModel(String prompt) {
        try {
            if (chatModel == null) {
                log.warn("ChatModel bean is not available; skipping LLM call");
                return null;
            }
            return chatModel.call(prompt);
        } catch (Exception e) {
            log.error("Error calling Gemini: {}", e.getMessage(), e);
            return null;
        }
    }

    public StudentSurveyResponseDto toResponseDto(StudentSurvey saved) {
        return StudentSurveyResponseDto.builder()
                .id(saved.getId())
                .studentId(saved.getStudent() != null ? saved.getStudent().getId() : null)
                .surveyVersion(saved.getSurveyVersion())
                .completedAt(saved.getCompletedAt())
                .createdAt(saved.getCreatedAt())
                .derivedInterestIds(saved.getDerivedInterests() != null ? saved.getDerivedInterests().stream().map(com.especlub.match.models.Interest::getId).collect(Collectors.toSet()) : Set.of())
                .derivedSoftSkillIds(saved.getDerivedSoftSkills() != null ? saved.getDerivedSoftSkills().stream().map(com.especlub.match.models.SoftSkill::getId).collect(Collectors.toSet()) : Set.of())
                .derivedClubReasonIds(saved.getDerivedClubReasons() != null ? saved.getDerivedClubReasons().stream().map(com.especlub.match.models.ClubReason::getId).collect(Collectors.toSet()) : Set.of())
                .build();
    }
}
