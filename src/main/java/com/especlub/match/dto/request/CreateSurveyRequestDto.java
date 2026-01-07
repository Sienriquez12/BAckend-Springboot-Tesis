package com.especlub.match.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateSurveyRequestDto {
    private String surveyVersion;
    private String rawAnswersJson;
    private List<Long> interestIds;
    private List<Long> softSkillIds;
    private List<Long> clubReasonIds;
    private Integer weeklyAvailabilityHours;
    private Integer maxParallelClubs;
    private Boolean recommendationOptIn;
    private Integer semesterNumber;
    private String preferredClubType;
    private String preferredMeetingFormat;
    private String shortTermGoal;
    private String longTermGoal;
    private Boolean isOpenToNewExperiences;
    private List<StudentPreferenceRequestDto> preferences;
}
