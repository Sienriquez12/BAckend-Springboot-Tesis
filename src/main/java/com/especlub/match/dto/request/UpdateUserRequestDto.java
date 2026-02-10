package com.especlub.match.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class UpdateUserRequestDto {
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String password;

    // When present: list of role IDs to set for the user.
    // null -> do not change roles; empty list -> clear roles
    private List<Long> roleIds;

    // Nuevo campo para indicar (al editar) si se asigna como presidente a un club
    private Long clubId;

    // Nuevo: cédula / national id para actualizar
    private String nationalId;
}