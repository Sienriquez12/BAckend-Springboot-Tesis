package com.especlub.match.models;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("Identificador único del evento")
    private Long id;

    @Column(name = "title", nullable = false)
    @Comment("Título breve del evento / charla")
    private String title;

    @Column(name = "description", length = 2000)
    @Comment("Descripción detallada del evento (agenda, temas, notas)")
    private String description;

    @Column(name = "start_at", nullable = false)
    @Comment("Fecha y hora de inicio del evento")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    @Comment("Fecha y hora de fin del evento (opcional)")
    private LocalDateTime endAt;

    @Column(name = "location")
    @Comment("Lugar físico del evento (opcional)")
    private String location;

    @Column(name = "virtual_link", length = 500)
    @Comment("Enlace a reunión virtual / streaming (opcional)")
    private String virtualLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    @Comment("Club al que pertenece el evento")
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_info_id", nullable = false)
    @Comment("Usuario que creó/la publicó")
    private UserInfo createdBy;

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