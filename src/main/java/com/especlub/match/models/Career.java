package com.especlub.match.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(name = "career")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("Identificador único de la carrera")
    private Long id;

    @Column(name = "code", length = 50, unique = true, nullable = false)
    @Comment("Código único de la carrera (por ejemplo, SIST, MED, ARQ)")
    private String code;

    @Column(name = "name", length = 200, nullable = false)
    @Comment("Nombre de la carrera")
    private String name;

    @Column(name = "faculty", length = 200)
    @Comment("Nombre de la facultad o departamento al que pertenece la carrera")
    private String faculty;

    @Column(name = "record_status")
    @Comment("Estado del registro (activo/inactivo)")
    private Boolean recordStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

