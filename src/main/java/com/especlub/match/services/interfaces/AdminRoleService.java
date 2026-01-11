package com.especlub.match.services.interfaces;

import com.especlub.match.models.UserRole;

import java.util.List;

public interface AdminRoleService {
    List<UserRole> listAllActive();
    UserRole getById(Long id);
}
