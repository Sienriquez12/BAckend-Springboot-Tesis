package com.especlub.match.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class CreateUserRequestDto {
    private String username;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String password;
    private LocalDate dateOfBirth;

    @NotNull(message = "Role ID is required")
    private Long roleId;

    // Nuevo campo para indicar al crear un presidente a qué club se asigna
    private Long clubId;
}