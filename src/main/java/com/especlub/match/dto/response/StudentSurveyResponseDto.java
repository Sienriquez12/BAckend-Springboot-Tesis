package com.especlub.match.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class StudentSurveyResponseDto {
    private Long id;
    private Long studentId;
    private String surveyVersion;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private Set<Long> derivedInterestIds;
    private Set<Long> derivedSoftSkillIds;
    private Set<Long> derivedClubReasonIds;
}
