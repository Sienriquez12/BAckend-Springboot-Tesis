package com.especlub.match.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateSurveyRequestDto {
    private String surveyVersion;
    private List<Long> interestIds; // Long -- entero
    private List<Long> softSkillIds;
    private List<Long> clubReasonIds;
    private Integer weeklyAvailabilityHours;
    private Integer maxParallelClubs;
    private Integer semesterNumber;
    // send the ClubType id instead of a String name
    private Long preferredClubTypeId;
    private String preferredMeetingFormat;
    private Boolean isOpenToNewExperiences;
    private Boolean recommendationOptIn;
    private List<StudentPreferenceRequestDto> preferences;
}
