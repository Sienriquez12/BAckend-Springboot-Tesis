package com.especlub.match.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClubMembersByClubDto {
    private Long clubId;
    private ClubAdminDto club;
    private List<ClubMemberAdminDto> members;
}
