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
    // Nuevo campo aditivo que contiene el presidente (si existe) — no rompe la respuesta existente ya que es adicional
    private ClubMemberAdminDto president;
}
