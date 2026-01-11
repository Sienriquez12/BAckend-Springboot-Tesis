package com.especlub.match.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.especlub.match.models.UserRole;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByName(String name);
    Optional<UserRole> findByIdAndRecordStatusTrue(Long id);
    List<UserRole> findAllByRecordStatusTrue();
}