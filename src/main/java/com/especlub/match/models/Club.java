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
@Table(name = "club")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("Identificador único del club")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name", nullable = false)
    @Comment("Nombre del club")
    private String name;

    @Column(name = "description", length = 2000)
    @Comment("Descripción del club")
    private String description;

    @Column(name = "capacity")
    @Comment("Capacidad máxima de miembros")
    private Integer capacity;

    private String whatsappGroupLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_type_id")
    @Comment("Tipo de club al que pertenece (Deportivo, Académico, Cultural, etc.)")
    private ClubType clubType;

    @ManyToMany
    @JoinTable(
            name = "club_reasons",
            joinColumns = @JoinColumn(name = "club_id"),
            inverseJoinColumns = @JoinColumn(name = "club_reason_id")
    )
    @ToString.Exclude
    private Set<ClubReason> reasons;

    @ManyToMany
    @JoinTable(
            name = "club_interests",
            joinColumns = @JoinColumn(name = "club_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id")
    )
    @ToString.Exclude
    private Set<Interest> interests;

    @ManyToMany
    @JoinTable(
            name = "club_soft_skills",
            joinColumns = @JoinColumn(name = "club_id"),
            inverseJoinColumns = @JoinColumn(name = "soft_skill_id")
    )
    @ToString.Exclude
    private Set<SoftSkill> desiredSoftSkills;

    /**
     * Use ClubMember entity to model membership details (status, timestamps, role, etc.).
     * mappedBy = "club" refers to ClubMember.club
     */
    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<ClubMember> members = new HashSet<>();

    @OneToOne(mappedBy = "club", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Comment("Perfil adicional del club para el motor de recomendaciones")
    @ToString.Exclude
    private ClubProfile profile;

    @Column(name = "record_status")
    @Comment("Estado del registro (activo/inactivo).")
    private Boolean recordStatus;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}