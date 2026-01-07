package com.especlub.match.dto.request;

import lombok.Data;

@Data
public class StudentPreferenceRequestDto {
    private String preferredClubType;
    private String avoidClubTypes;
    private String preferredMeetingFormat;
    private Integer priorityWeight;
}

