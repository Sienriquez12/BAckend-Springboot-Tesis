package com.especlub.match.services;

import com.especlub.match.dto.request.CreateClubRequestDto;
import com.especlub.match.dto.request.UpdateClubRequestDto;
import com.especlub.match.dto.response.ClubAdminDto;
import com.especlub.match.dto.response.ClubMemberAdminDto;

import java.util.List;

public interface AdminClubService {
    List<ClubAdminDto> listAll();
    ClubAdminDto getById(Long id);
    ClubAdminDto create(CreateClubRequestDto dto);
    ClubAdminDto update(Long id, UpdateClubRequestDto dto);
    boolean delete(Long id);

    // Lista miembros activos de un club
    List<ClubMemberAdminDto> listMembers(Long clubId);
}
