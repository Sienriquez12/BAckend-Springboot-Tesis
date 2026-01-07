package com.especlub.match.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("Identificador único del registro de auditoría.")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Comment("Usuario que realizó la acción.")
    private UserInfo user;

    @Column(name = "action")
    @Comment("Agente o sistema que realizó la acción.")
    private String userAgent;

    @Column(name = "ip_address")
    @Comment("Dirección IP desde donde se realizó la acción.")
    private String ipAddress;

    @Column(name = "http_method")
    @Comment("Tipo de método HTTP (GET, POST, PUT, DELETE, etc.).")
    private String httpMethod;

    @Column(name = "endpoint")
    @Comment("Endpoint o recurso accedido.")
    private String endpoint;

    @Column(name = "request_body", columnDefinition = "TEXT")
    @Comment("Cuerpo de la petición (si aplica).")
    private String requestBody;

    @Column(name = "response_body", columnDefinition = "TEXT")
    @Comment("Cuerpo de la respuesta (si aplica).")
    private String responseBody;

    @Column(name = "status_code")
    @Comment("Código de estado HTTP de la respuesta.")
    private Integer statusCode;

    @CreationTimestamp
    @Column(name = "action_date")
    @Comment("Fecha y hora en que se realizó la acción.")
    private LocalDateTime actionDate;
}
