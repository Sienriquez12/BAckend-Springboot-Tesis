package com.especlub.match.services.impl;
import com.especlub.match.shared.exceptions.CustomExceptions;
import com.especlub.match.models.UserInfo;
import com.especlub.match.repositories.UserInfoRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements com.especlub.match.services.interfaces.UserService {

    private final UserInfoRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    // Crear usuario
    public UserInfo createUser(UserInfo user) {
        if(userRepo.existsByUsername(user.getUsername()))
            throw new CustomExceptions("Username already exists", 404);
        if(userRepo.existsByEmail(user.getEmail()))
            throw new CustomExceptions("Email already exists", 404);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public List<UserInfo> getAllUsers() {
        return userRepo.findAll().stream()
                .filter(u -> Boolean.TRUE.equals(u.getRecordStatus()))
                .toList();
    }

    public Optional<UserInfo> getUserById(Long id) {
        Optional<UserInfo> user = userRepo.findById(id);
        if (user.isPresent() && Boolean.TRUE.equals(user.get().getRecordStatus())) {
            return user;
        }
        return Optional.empty();
    }

    public UserInfo updateUser(Long id, UserInfo user) {
        UserInfo existing = userRepo.findById(id)
                .filter(u -> Boolean.TRUE.equals(u.getRecordStatus()))
                .orElseThrow(() -> new CustomExceptions("User not found or inactive", 404));
        existing.setUsername(user.getUsername());
        existing.setEmail(user.getEmail());
        existing.setNames(user.getNames());
        existing.setSurnames(user.getSurnames());
        existing.setPhone(user.getPhone());
        existing.setRoles(user.getRoles());
        existing.setRecordStatus(user.getRecordStatus());
        return userRepo.save(existing);
    }

    public void deleteUser(Long id) {
        UserInfo user = userRepo.findById(id)
                .filter(u -> Boolean.TRUE.equals(u.getRecordStatus()))
                .orElseThrow(() -> new CustomExceptions("User not found or inactive", 404));
        user.setRecordStatus(false);
        userRepo.save(user);
    }

}