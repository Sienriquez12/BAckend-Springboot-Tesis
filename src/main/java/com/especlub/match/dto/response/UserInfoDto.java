package com.especlub.match.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDto {
    private Long id;
    private String username;
    private String email;
    private String names; // First name(s) of the user
    private String surnames; // Last name(s) of the user
}
