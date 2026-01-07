package com.especlub.match.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class CreateClubRequestDto {
    private String name;
    private String description;
    private Integer capacity;
    private Set<Long> reasonIds;
    private Set<Long> interestIds;
    private Set<Long> desiredSoftSkillIds;
}

