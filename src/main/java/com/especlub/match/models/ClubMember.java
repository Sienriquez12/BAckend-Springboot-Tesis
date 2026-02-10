package com.especlub.match.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "club_member", uniqueConstraints = {@UniqueConstraint(name = "uk_club_student", columnNames = {"club_id","student_id"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("Identificador único de la membresía")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "club_id", nullable = false)
    @Comment("Referencia al club")
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    @Comment("Referencia al estudiante (miembro)")
    private Student student;

    @Column(name = "record_status")
    @Comment("Estado del registro (activo/inactivo).")
    private Boolean recordStatus;

    // Nuevo: marca si esta membresía corresponde al presidente del club
    @Column(name = "is_president")
    @Comment("Indica si este miembro es el presidente del club")
    private Boolean isPresident;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /**
     * Convenience accessor used by services to obtain the user's contact info.
     * Returns the related UserInfo from the Student or null if not available.
     */
    @Transient
    public UserInfo getUserInfo() {
        return student != null ? student.getUserInfo() : null;
    }
}