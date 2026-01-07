package com.especlub.match.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(name = "club_profile")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("Identificador único del perfil del club")
    @EqualsAndHashCode.Include
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false, unique = true)
    @Comment("Relación uno a uno con el club")
    @ToString.Exclude
    private Club club;

    @Column(name = "club_type", length = 100)
    @Comment("Tipo de club: Académico, Deportivo, Cultural, Voluntariado, etc.")
    private String clubType;

    @Column(name = "target_careers", length = 500)
    @Comment("Carreras objetivo del club (texto libre o lista separada por comas)")
    private String targetCareers;

    @Column(name = "min_semester")
    @Comment("Semestre mínimo recomendado para los estudiantes")
    private Integer minSemester;

    @Column(name = "max_semester")
    @Comment("Semestre máximo recomendado para los estudiantes")
    private Integer maxSemester;

    @Column(name = "expected_weekly_commitment_hours")
    @Comment("Horas de compromiso semanal esperadas por el club")
    private Integer expectedWeeklyCommitmentHours;

    @Column(name = "accepts_beginners")
    @Comment("Indica si el club acepta principiantes sin experiencia previa")
    private Boolean acceptsBeginners;

    @Column(name = "is_active_for_recommendation")
    @Comment("Indica si este club está activo para el motor de recomendaciones")
    private Boolean isActiveForRecommendation;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
