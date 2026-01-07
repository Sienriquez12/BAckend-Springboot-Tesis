package com.especlub.match.dto.request;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAdminDto {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
}