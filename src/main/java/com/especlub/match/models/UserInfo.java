package com.especlub.match.models;


import com.especlub.match.shared.validations.annotations.CustomEcuadorCedula;
import com.especlub.match.shared.validations.annotations.CustomOnlyDigits;
import com.especlub.match.shared.validations.annotations.CustomPasswordSecure;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("Identificador único del usuario.")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "username", nullable = false)
    @Comment("Nombre de usuario para login.")
    private String username;

    @Column(name = "email")
    @Comment("Correo electrónico del usuario.")
    private String email;

    @Column(name = "phone", length = 50)
    @Comment("Teléfono del usuario.")
    private String phone;

    @Column(name = "first_name")
    @Comment("Nombres del usuario.")
    private String names;

    @Column(name = "last_name")
    @Comment("Apellido del usuario.")
    private String surnames;

    @Column(name = "birth_date")
    @Comment("Fecha de nacimiento del usuario.")
    private LocalDate birthDate;

    @Column(name = "first_login")
    @Comment("Indica si es el primer inicio de sesión del usuario.")
    private Boolean firstLogin;

    @AssertTrue
    @Comment("Indica si debe aceptar los términos y condiciones.")
    private Boolean acceptTerms;

    @AssertTrue
    @Comment("Indica si debe aceptar la política de privacidad.")
    private Boolean acceptPrivacy;

    @Size(min = 10, max = 10, message = "La cédula debe tener 10 dígitos")
    @CustomOnlyDigits(message = "La cédula solo debe contener números")
    @CustomEcuadorCedula(message = "La cédula no es válida para Ecuador")
    private String nationalId;

    @Column(name = "password")
    @Comment("Contraseña encriptada.")
    @NotBlank(message = "La contraseña no puede estar vacía")
    @CustomPasswordSecure(message = "La contraseña no cumple con los requisitos de seguridad")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    private String password;

    @Column(name = "record_status")
    @Comment("Estado del registro (activo/inactivo).")
    private Boolean recordStatus;

    @Column(name = "created_at")
    @Comment("Fecha de creación.")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Comment("Fecha de última actualización.")
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_info_roles",
        joinColumns = @JoinColumn(name = "user_info_id"),
        inverseJoinColumns = @JoinColumn(name = "user_role_id")
    )
    private List<UserRole> roles;
}