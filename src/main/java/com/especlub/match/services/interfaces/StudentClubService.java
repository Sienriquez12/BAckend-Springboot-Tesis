package com.especlub.match.services.interfaces;

import com.especlub.match.dto.response.ClubAdminDto;

import java.util.List;

public interface StudentClubService {
    // userInfoId: id de la entidad UserInfo que realiza la acción (puede ser estudiante, docente, etc.)
    String enrollStudent(Long userInfoId, Long clubId);
    void leaveClub(Long userInfoId, Long clubId);

    // Nuevo método para obtener clubs del usuario
    List<ClubAdminDto> findClubsByUserInfoId(Long userInfoId);
}