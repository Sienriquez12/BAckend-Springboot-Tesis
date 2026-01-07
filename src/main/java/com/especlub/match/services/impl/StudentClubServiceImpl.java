package com.especlub.match.services.impl;

import com.especlub.match.models.Club;
import com.especlub.match.models.ClubMember;
import com.especlub.match.models.Student;
import com.especlub.match.models.UserInfo;
import com.especlub.match.repositories.ClubMemberRepository;
import com.especlub.match.repositories.ClubRepository;
import com.especlub.match.repositories.StudentRepository;
import com.especlub.match.repositories.UserInfoRepository;
import com.especlub.match.services.interfaces.StudentClubService;
import com.especlub.match.shared.exceptions.CustomExceptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentClubServiceImpl implements StudentClubService {

    private final ClubRepository clubRepository;
    private final StudentRepository studentRepository;
    private final UserInfoRepository userInfoRepository;
    private final ClubMemberRepository clubMemberRepository;

    @Override
    @Transactional
    public String enrollStudent(Long userInfoId, Long clubId) {
        log.debug("enrollStudent: starting for userInfoId={} clubId={}", userInfoId, clubId);
        Club club = clubRepository.findByIdAndRecordStatusTrue(clubId)
                .orElseThrow(() -> new CustomExceptions("Club no encontrado o inactivo", 404));
        log.debug("enrollStudent: club found id={} name={} capacity={}", club.getId(), club.getName(), club.getCapacity());

        // buscar Student por userInfoId
        Student student = studentRepository.findByUserInfo_IdAndRecordStatusTrue(userInfoId)
                .orElseGet(() -> {
                    log.debug("enrollStudent: no student found for userInfoId={}, creating new student", userInfoId);
                    // intentar obtener UserInfo
                    UserInfo u = userInfoRepository.findById(userInfoId).orElseThrow(() -> new CustomExceptions("Usuario no encontrado", 404));
                    // crear nuevo Student asociado
                    Student s = Student.builder()
                            .userInfo(u)
                            .recordStatus(true)
                            .build();
                    Student saved = studentRepository.save(s);
                    log.debug("enrollStudent: created new student id={} for userInfoId={}", saved.getId(), userInfoId);
                    return saved;
                });

        log.debug("enrollStudent: student found id={} userInfoId={}", student.getId(), student.getUserInfo() != null ? student.getUserInfo().getId() : null);

        // Check via repository if an active membership already exists
        boolean exists = clubMemberRepository.existsByClubIdAndStudentIdAndRecordStatusTrue(clubId, student.getId());
        if (exists) {
            log.debug("enrollStudent: membership already exists (repository) for studentId={} clubId={}", student.getId(), clubId);
            throw new CustomExceptions("Usuario ya inscrito en el club", 400);
        }

        long activeCount = clubMemberRepository.findAllByClubIdAndRecordStatusTrue(clubId).stream()
                .filter(cm -> Boolean.TRUE.equals(cm.getRecordStatus()))
                .count();

        log.debug("enrollStudent: activeCount={} capacity={}", activeCount, club.getCapacity());

        if (club.getCapacity() != null && activeCount >= club.getCapacity()) {
            log.warn("enrollStudent: cannot enroll, club full clubId={} capacity={} activeCount={}", clubId, club.getCapacity(), activeCount);
            throw new CustomExceptions("El club ya alcanzó su capacidad", 400);
        }

        ClubMember membership = ClubMember.builder()
                .club(club)
                .student(student)
                .recordStatus(true)
                .build();

        // persist membership via repository
        ClubMember savedMembership = clubMemberRepository.save(membership);

        // update in-memory collections for consistency
        if (club.getMembers() == null) club.setMembers(new HashSet<>());
        if (student.getMemberships() == null) student.setMemberships(new HashSet<>());
        club.getMembers().add(savedMembership);
        student.getMemberships().add(savedMembership);

        log.debug("enrollStudent: membership created for studentId={} clubId={} membershipId={}", student.getId(), clubId, savedMembership.getId());

        return club.getWhatsappGroupLink();
    }

    @Override
    @Transactional
    public void leaveClub(Long userInfoId, Long clubId) {
        log.debug("leaveClub: starting for userInfoId={} clubId={}", userInfoId, clubId);
        Club club = clubRepository.findByIdAndRecordStatusTrue(clubId)
                .orElseThrow(() -> new CustomExceptions("Club no encontrado o inactivo", 404));

        Student student = studentRepository.findByUserInfo_IdAndRecordStatusTrue(userInfoId)
                .orElseThrow(() -> new CustomExceptions("Estudiante no encontrado o inactivo", 404));

        log.debug("leaveClub: found studentId={} clubId={}", student.getId(), clubId);

        Optional<ClubMember> membershipOpt = clubMemberRepository.findByClubIdAndStudentIdAndRecordStatusTrue(clubId, student.getId());

        if (membershipOpt.isPresent()) {
            ClubMember cm = membershipOpt.get();
            // mark as inactive or delete
            cm.setRecordStatus(false);
            clubMemberRepository.save(cm);
            log.debug("leaveClub: membership deactivated studentId={} clubId={} membershipId={}", student.getId(), clubId, cm.getId());
            return;
        } else {
            log.warn("leaveClub: membership not found for studentId={} clubId={}", student.getId(), clubId);
            throw new CustomExceptions("El estudiante no está inscrito en el club", 400);
        }
    }
}