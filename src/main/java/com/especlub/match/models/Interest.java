package com.especlub.match.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "interest")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Interest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("Identificador único del interés")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @Comment("Nombre del interés, e.g. Música, Tecnología")
    private String name;

    @Column(name = "description")
    @Comment("Descripción opcional del interés")
    private String description;

    @Column(name = "record_status")
    @Comment("Estado del registro (activo/inactivo).")
    private Boolean recordStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonIgnore
    @ManyToMany(mappedBy = "interests")
    @ToString.Exclude
    private Set<Student> students;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Interest)) return false;
        Interest other = (Interest) o;
        if (this.id == null || other.id == null) return this == other;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return (id != null) ? id.hashCode() : System.identityHashCode(this);
    }
}
