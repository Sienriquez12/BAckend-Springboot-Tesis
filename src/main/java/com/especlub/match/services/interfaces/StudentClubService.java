package com.especlub.match.services.interfaces;

public interface StudentClubService {
    // userInfoId: id de la entidad UserInfo que realiza la acción (puede ser estudiante, docente, etc.)
    String enrollStudent(Long userInfoId, Long clubId);
    void leaveClub(Long userInfoId, Long clubId);
}