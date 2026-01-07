package com.especlub.match.dto.response;


import com.especlub.match.models.UserRole;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UserAuthDto {

    private Long id;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String password;

    @Min(value = 0, message = "El número de intentos fallidos no puede ser negativo")
    private int failedLoginAttempts = 0;

    private LocalDateTime lastLoginAt;

    private LocalDateTime passwordChangedAt;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;


    private boolean statusRegistration = true;

    private boolean locked = false;

    private Set<UserRole> roles;
}
