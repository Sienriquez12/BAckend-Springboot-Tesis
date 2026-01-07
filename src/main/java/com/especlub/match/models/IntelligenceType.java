package com.especlub.match.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "intelligence_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class IntelligenceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Comment("Identificador único del tipo de inteligencia")
    private Long id;

    @Column(name = "code", length = 50, unique = true)
    @Comment("Código corto del tipo, e.g. LINGUISTIC, LOGICAL")
    private String code;

    @Column(name = "name", length = 150, nullable = false)
    @Comment("Nombre legible del tipo de inteligencia")
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "record_status")
    private Boolean recordStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "intelligenceType", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private Set<MultipleIntelligenceQuestion> questions = new HashSet<>();
}

