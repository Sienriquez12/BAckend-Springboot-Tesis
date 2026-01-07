package com.especlub.match.models;

import com.especlub.match.models.UserInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_pin")
public class UserPin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("Identificador único del PIN")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Comment("Usuario asociado al PIN")
    private UserInfo user;

    @Column(name = "pin", nullable = false, length = 10)
    @Comment("Código PIN generado para recuperación o verificación")
    private String pin;

    @Column(name = "used", nullable = false)
    @Comment("Indica si el PIN ya fue usado")
    private Boolean used;

    @Column(name = "created_at", nullable = false)
    @Comment("Fecha y hora de creación del PIN")
    private LocalDateTime createdAt;

    @Column(name = "used_at")
    @Comment("Fecha y hora en que el PIN fue usado")
    private LocalDateTime usedAt;

    @Column(name = "expires_at")
    @Comment("Fecha y hora de expiración del PIN")
    private LocalDateTime expiresAt;

    @Column(name = "purpose", length = 30)
    @Comment("Propósito del PIN: RECOVERY, VERIFICATION, etc.")
    private String purpose;
}

