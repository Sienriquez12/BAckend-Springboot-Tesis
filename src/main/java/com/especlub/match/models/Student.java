package com.especlub.match.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("Identificador único del estudiante")
    @EqualsAndHashCode.Include
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_info_id", nullable = false, unique = true)
    @Comment("Relación uno a uno con la tabla user_info")
    private UserInfo userInfo;

    @ManyToMany(targetEntity = Interest.class)
    @JoinTable(
            name = "student_interests",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id")
    )
    private Set<Interest> interests;

    /**
     * Replace previous ManyToMany to clubs with ClubMember association.
     * mappedBy = "student" refers to ClubMember.student
     */
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClubMember> memberships = new HashSet<>();

    @ManyToMany(targetEntity = SoftSkill.class)
    @JoinTable(
            name = "student_soft_skills",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "soft_skill_id")
    )
    private Set<SoftSkill> softSkills;

    @ManyToMany(targetEntity = ClubReason.class)
    @JoinTable(
            name = "student_preferred_reasons",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "club_reason_id")
    )
    private Set<ClubReason> preferredReasons;

    // Preferencias explícitas adicionales (tipo de club, formato, etc.)
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StudentPreference> preferences = new HashSet<>();

    @Column(name = "profile", length = 2000)
    @Comment("Descripción breve / intereses adicionales del estudiante")
    private String profile;

    @Column(name = "record_status")
    @Comment("Estado del registro (activo/inactivo).")
    private Boolean recordStatus;

    @Column(name = "academic_level")
    private String academicLevel;

    // Información académica y de contexto para recomendaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "career_id")
    @Comment("Carrera o programa académico del estudiante")
    private Career career;

    @Column(name = "semester_number")
    @Comment("Número de semestre actual del estudiante")
    private Integer semesterNumber;

    @Column(name = "entry_year")
    @Comment("Año de ingreso del estudiante a la universidad")
    private Integer entryYear;

    @Column(name = "weekly_availability_hours")
    @Comment("Horas aproximadas que el estudiante puede dedicar a clubes por semana")
    private Integer weeklyAvailabilityHours;

    @Column(name = "preferred_club_type", length = 100)
    @Comment("Tipo de club preferido: Académico, Deportivo, Cultural, etc.")
    private String preferredClubType;

    @Column(name = "max_parallel_clubs")
    @Comment("Número máximo de clubes que el estudiante desea tener en paralelo")
    private Integer maxParallelClubs;

    @Column(name = "is_open_to_new_experiences")
    @Comment("Indica si el estudiante está abierto a explorar clubes fuera de sus intereses principales")
    private Boolean isOpenToNewExperiences;

    @Column(name = "short_term_goal", length = 500)
    @Comment("Meta u objetivo principal a corto plazo al unirse a un club")
    private String shortTermGoal;

    @Column(name = "long_term_goal", length = 1000)
    @Comment("Meta u objetivo a mediano/largo plazo relacionada con su participación en clubes")
    private String longTermGoal;

    @Column(name = "recommendation_opt_in")
    @Comment("Indica si el estudiante acepta recibir recomendaciones de clubes")
    private Boolean recommendationOptIn;

    @Column(name = "last_recommendation_at")
    @Comment("Fecha de la última recomendación generada para este estudiante")
    private LocalDateTime lastRecommendationAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}