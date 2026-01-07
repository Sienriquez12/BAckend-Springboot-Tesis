package com.especlub.match.repositories;

import com.especlub.match.models.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for UserInfo MongoDB collection.
 */
@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    @Query("SELECT u FROM UserInfo u WHERE u.id = :id AND u.recordStatus = true")
    Optional<UserInfo> findByIdAndRecordStatusTrue(@Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserInfo u WHERE u.username = :username")
    boolean existsByUsername(@Param("username") String username);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserInfo u WHERE u.username = :username AND u.recordStatus = true")
    boolean existsByUsernameAndRecordStatusTrue(@Param("username") String username);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserInfo u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserInfo u WHERE u.email = :email AND u.recordStatus = true")
    boolean existsByEmailAndRecordStatusTrue(@Param("email") String email);

    @Query("SELECT u FROM UserInfo u WHERE u.email = :email AND u.recordStatus = false")
    UserInfo findByEmailAndRecordStatusFalse(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserInfo u WHERE u.nationalId = :nationalId")
    boolean existsByNationalId(@Param("nationalId") String nationalId);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserInfo u WHERE u.nationalId = :nationalId AND u.recordStatus = true")
    boolean existByNationalIdAndRecordStatusTrue(String nationalId);

    @Query("SELECT u FROM UserInfo u WHERE u.username = :username AND u.recordStatus = true")
    UserInfo findByUsernameAndRecordStatusTrue(@Param("username") String username);

    @Query("SELECT u FROM UserInfo u WHERE u.email = :email AND u.recordStatus = true")
    UserInfo findByEmailAndRecordStatusTrue(@Param("email") String email);

    @Query("SELECT u FROM UserInfo u WHERE u.nationalId = :nationalId AND u.email = :email AND u.recordStatus = true")
    UserInfo findByNationalIdAndEmailAndRecordStatusTrue(@Param("nationalId") String nationalId, @Param("email") String email);

    @Query("SELECT u FROM UserInfo u WHERE u.username = :username AND u.recordStatus = false")
    UserInfo findByUsernameAndRecordStatusFalse(@Param("username") String username);

    // Nuevo: retornar todos los usuarios activos directamente desde la DB
    @Query("SELECT u FROM UserInfo u WHERE u.recordStatus = true")
    List<UserInfo> findAllByRecordStatusTrue();
}
