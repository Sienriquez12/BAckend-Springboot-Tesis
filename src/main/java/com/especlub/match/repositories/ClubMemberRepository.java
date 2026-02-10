package com.especlub.match.repositories;

import com.especlub.match.models.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

    @Query("select cm from ClubMember cm " +
            "join fetch cm.student s " +
            "join fetch s.userInfo ui " +
            "where cm.club.id = :clubId and cm.recordStatus = true")
    List<ClubMember> findAllByClubIdAndRecordStatusTrue(@Param("clubId") Long clubId);

    @Query("select cm from ClubMember cm " +
            "join fetch cm.student s " +
            "left join fetch s.userInfo ui " +
            "where cm.recordStatus = true")
    List<ClubMember> findAllActiveWithStudentAndUserInfo();

    @Query("select cm from ClubMember cm " +
            "join fetch cm.club c " +
            "where cm.student.id = :studentId and cm.recordStatus = true")
    List<ClubMember> findAllByStudentIdAndRecordStatusTrue(@Param("studentId") Long studentId);

    // Nuevo: existencia de membresía activa por club y student
    @Query("select case when count(cm) > 0 then true else false end from ClubMember cm where cm.club.id = :clubId and cm.student.id = :studentId and cm.recordStatus = true")
    boolean existsByClubIdAndStudentIdAndRecordStatusTrue(@Param("clubId") Long clubId, @Param("studentId") Long studentId);

    // Nuevo: buscar una membresía activa por club y student
    @Query("select cm from ClubMember cm where cm.club.id = :clubId and cm.student.id = :studentId and cm.recordStatus = true")
    Optional<ClubMember> findByClubIdAndStudentIdAndRecordStatusTrue(@Param("clubId") Long clubId, @Param("studentId") Long studentId);

    // Nuevo: buscar presidente actual (si existe) por club
    @Query("select cm from ClubMember cm where cm.club.id = :clubId and cm.isPresident = true and cm.recordStatus = true")
    Optional<ClubMember> findPresidentByClubId(@Param("clubId") Long clubId);
}