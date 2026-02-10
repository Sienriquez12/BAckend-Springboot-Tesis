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
}