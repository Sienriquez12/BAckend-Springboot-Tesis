package com.especlub.match.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "club_reason")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubReason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("Identificador único de la razón del club")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @Comment("Nombre de la razón, e.g. Social, Económico, Espiritual")
    private String name;

    @Column(name = "description")
    @Comment("Descripción opcional de la razón")
    private String description;

    @Column(name = "record_status")
    @Comment("Estado del registro (activo/inactivo).")
    private Boolean recordStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonIgnore
    @ManyToMany(mappedBy = "reasons")
    private Set<Club> clubs;

    @JsonIgnore
    @ManyToMany(mappedBy = "preferredReasons")
    private Set<Student> students;
}
