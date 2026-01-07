package com.especlub.match.repositories;

import com.especlub.match.models.UserInfo;
import com.especlub.match.models.UserPin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPinRepository extends JpaRepository<UserPin, Long> {
    @Query("SELECT p FROM UserPin p WHERE p.user = :user AND p.purpose = :purpose AND p.used = false AND p.expiresAt > CURRENT_TIMESTAMP")
    List<UserPin> findActivePinsByUserAndPurpose(@Param("user") UserInfo user, @Param("purpose") String purpose);

    Optional<UserPin> findByUserAndPinAndPurpose(UserInfo user, String pin, String purpose);

    List<UserPin> findAllByUser(UserInfo user);
}
