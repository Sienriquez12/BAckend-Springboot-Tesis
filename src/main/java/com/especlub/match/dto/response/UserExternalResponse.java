package com.especlub.match.dto.response;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * User Data Transfer Object (DTO) for transferring user data between layers.
 * This class is used to encapsulate user information and validation constraints.
 * This using in Controller - Response
 */
@Data
public class UserExternalResponse {
    private String username;
    private Set<RoleExternalResponse> roles = new HashSet<>();
}
