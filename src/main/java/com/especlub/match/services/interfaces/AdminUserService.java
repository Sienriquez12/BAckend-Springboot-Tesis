package com.especlub.match.services.interfaces;

import com.especlub.match.dto.request.CreateUserRequestDto;
import com.especlub.match.dto.request.UpdateUserRequestDto;
import com.especlub.match.dto.request.UserAdminDto;

import java.util.List;

public interface AdminUserService {
    List<UserAdminDto> listAllActive();
    UserAdminDto getById(Long id);
    UserAdminDto create(CreateUserRequestDto dto);
    UserAdminDto update(Long id, UpdateUserRequestDto dto);
    boolean delete(Long id);
}