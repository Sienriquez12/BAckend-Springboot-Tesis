package com.especlub.match.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ClubMemberAdminDto {
    private Long membershipId;
    private Long studentId;
    private Long userInfoId;
    private String email;
    private String fullName;
    private Boolean recordStatus;
    private LocalDateTime joinedAt;
    private List<ClubSummaryDto> clubs;
    private Boolean isPresident;
}
