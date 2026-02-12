package com.especlub.match.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClubMembersWithPresidentDto {
    private List<ClubMemberAdminDto> members;
    private ClubMemberAdminDto president; // nullable
}

