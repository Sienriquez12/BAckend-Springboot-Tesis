package com.especlub.match.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventAdminDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String location;
    private String virtualLink;
    private Long clubId;
    private Long createdByUserInfoId;
    private Boolean recordStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}