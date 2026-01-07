package com.especlub.match.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_preference")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("Identificador único de la preferencia del estudiante")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @Comment("Estudiante al que pertenece esta preferencia")
    private Student student;

    @Column(name = "preferred_club_type", length = 100)
    @Comment("Tipo de club preferido: Académico, Deportivo, Cultural, etc.")
    private String preferredClubType;

    @Column(name = "avoid_club_types", length = 500)
    @Comment("Tipos de club a evitar (texto libre o lista separada por comas)")
    private String avoidClubTypes;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_meeting_format", length = 50)
    @Comment("Formato de reunión preferido: Presencial, Virtual, Híbrido")
    private MeetingFormat preferredMeetingFormat;

    @Column(name = "priority_weight")
    @Comment("Peso/prioridad de esta preferencia en el motor de recomendación")
    private Integer priorityWeight;

    @Column(name = "record_status")
    @Comment("Estado del registro (activo/inactivo)")
    private Boolean recordStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
