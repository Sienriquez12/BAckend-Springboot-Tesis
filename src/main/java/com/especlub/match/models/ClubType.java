package com.especlub.match.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "club_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ClubType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("Identificador único del tipo de club")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @Comment("Nombre del tipo de club (ej: Deportivo, Académico, Cultural)")
    private String name;

    @Column(name = "description", length = 1000)
    @Comment("Descripción del tipo de club y su enfoque")
    private String description;

    @Column(name = "order_index")
    @Comment("Orden de visualización en listas")
    private Integer orderIndex;

    @OneToMany(mappedBy = "clubType", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Club> clubs = new HashSet<>();

    @Column(name = "record_status")
    @Comment("Estado del registro (activo/inactivo)")
    private Boolean recordStatus;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
