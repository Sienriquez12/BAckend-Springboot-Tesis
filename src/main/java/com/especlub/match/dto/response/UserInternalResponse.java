package com.especlub.match.dto.response;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

/**
 * Response DTO for user information.
 * This class contains user details such as username, password, registration status,
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInternalResponse {

    private String username;

    @JsonIgnore
    private String password;
    private Boolean registrationStatus;
    private LocalDate creationDate;
    private LocalDate updateDate;
    private Set<String> roles;

}
