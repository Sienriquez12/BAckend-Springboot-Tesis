package com.especlub.match.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "student_survey")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentSurvey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("Identificador único de la encuesta del estudiante")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    @Comment("Relación uno a uno con el estudiante (encuesta principal de perfil)")
    private Student student;

    @Column(name = "raw_answers_json", columnDefinition = "TEXT")
    @Comment("Respuestas crudas de la encuesta en formato JSON o prompt enviado")
    private String rawAnswersJson;

    @Column(name = "llm_response", columnDefinition = "TEXT")
    @Comment("Respuesta generada por el LLM (Gemini) en texto o JSON")
    private String llmResponse;

    @ManyToMany
    @JoinTable(
            name = "student_survey_interests",
            joinColumns = @JoinColumn(name = "student_survey_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id")
    )
    private Set<Interest> derivedInterests;

    @ManyToMany
    @JoinTable(
            name = "student_survey_soft_skills",
            joinColumns = @JoinColumn(name = "student_survey_id"),
            inverseJoinColumns = @JoinColumn(name = "soft_skill_id")
    )
    private Set<SoftSkill> derivedSoftSkills;

    @ManyToMany
    @JoinTable(
            name = "student_survey_club_reasons",
            joinColumns = @JoinColumn(name = "student_survey_id"),
            inverseJoinColumns = @JoinColumn(name = "club_reason_id")
    )
    private Set<ClubReason> derivedClubReasons;

    @Column(name = "survey_version", length = 20)
    @Comment("Versión de la encuesta aplicada")
    private String surveyVersion;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
