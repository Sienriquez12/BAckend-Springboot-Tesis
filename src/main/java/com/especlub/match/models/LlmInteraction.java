package com.especlub.match.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "llm_interaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LlmInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("Identificador único de la interacción con el LLM")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_info_id")
    @Comment("Usuario que solicitó la recomendación")
    private UserInfo user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_survey_id")
    @Comment("Encuesta relacionada (opcional)")
    private StudentSurvey studentSurvey;

    @Column(name = "model", length = 100)
    @Comment("Modelo LLM usado (ej. gemini)")
    private String model;

    @Column(name = "prompt", columnDefinition = "TEXT")
    @Comment("Prompt enviado al LLM")
    private String prompt;

    @Column(name = "response", columnDefinition = "TEXT")
    @Comment("Respuesta recibida del LLM")
    private String response;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

