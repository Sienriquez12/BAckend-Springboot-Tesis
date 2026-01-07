package com.especlub.match.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(name = "mi_question")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MultipleIntelligenceQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Comment("Identificador único de la pregunta")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intelligence_type_id")
    @ToString.Exclude
    @JsonIgnore
    private IntelligenceType intelligenceType;

    @Column(name = "code", length = 100, unique = true)
    private String code;

    @Column(name = "text", length = 2000)
    private String text;

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(name = "record_status")
    private Boolean recordStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
