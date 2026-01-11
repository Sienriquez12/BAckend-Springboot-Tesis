package com.especlub.match.services.impl;

import com.especlub.match.models.UserRole;
import com.especlub.match.repositories.UserRoleRepository;
import com.especlub.match.services.interfaces.AdminRoleService;
import com.especlub.match.shared.exceptions.CustomExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminRoleServiceImpl implements AdminRoleService {

    private final UserRoleRepository userRoleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserRole> listAllActive() {
        return userRoleRepository.findAllByRecordStatusTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public UserRole getById(Long id) {
        return userRoleRepository.findByIdAndRecordStatusTrue(id)
                .orElseThrow(() -> new CustomExceptions("Role not found", 404));
    }
}
