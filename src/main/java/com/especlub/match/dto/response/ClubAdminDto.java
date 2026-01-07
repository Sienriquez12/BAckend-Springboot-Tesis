package com.especlub.match.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ClubAdminDto {
    private Long id;
    private String name;
    private String description;
    private Integer capacity;
    private Set<Long> reasonIds;
    private Set<String> reasonNames;
    private Set<Long> interestIds;
    private Set<String> interestNames;
    private Set<Long> desiredSoftSkillIds;
    private Set<String> desiredSoftSkillNames;
}
