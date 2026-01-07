package com.especlub.match.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("Identificador único del rol.")
    private Long id;

    @Column(name = "name", nullable = false)
    @Comment("Nombre del rol.")
    private String name;

    @Column(name = "description")
    @Comment("Descripción del rol.")
    private String description;

    @Column(name = "hierarchy")
    @Comment("Nivel de jerarquía del rol.")
    private Integer hierarchy;

    @Column(name = "is_active")
    @Comment("Indica si el rol está activo.")
    private Boolean isActive;

    @Column(name = "record_status")
    @Comment("Estado del registro (activo/inactivo).")
    private Boolean recordStatus;

    @Column(name = "created_at")
    @Comment("Fecha de creación.")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Comment("Fecha de última actualización.")
    private LocalDateTime updatedAt;

}