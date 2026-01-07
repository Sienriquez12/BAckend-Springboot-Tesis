package com.especlub.match.services.impl;

import com.especlub.match.dto.request.CreateClubRequestDto;
import com.especlub.match.dto.request.UpdateClubRequestDto;
import com.especlub.match.dto.response.ClubAdminDto;
import com.especlub.match.dto.response.ClubMemberAdminDto;
import com.especlub.match.models.Club;
import com.especlub.match.models.ClubMember;
import com.especlub.match.models.ClubReason;
import com.especlub.match.models.Interest;
import com.especlub.match.models.SoftSkill;
import com.especlub.match.repositories.ClubMemberRepository;
import com.especlub.match.repositories.ClubReasonRepository;
import com.especlub.match.repositories.ClubRepository;
import com.especlub.match.repositories.InterestRepository;
import com.especlub.match.repositories.SoftSkillRepository;
import com.especlub.match.services.AdminClubService;
import com.especlub.match.shared.exceptions.CustomExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminClubServiceImpl implements AdminClubService {

    private final ClubRepository clubRepository;
    private final ClubReasonRepository clubReasonRepository;
    private final InterestRepository interestRepository;
    private final SoftSkillRepository softSkillRepository;
    private final ClubMemberRepository clubMemberRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ClubAdminDto> listAll() {
        return clubRepository.findAllByRecordStatusTrue()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClubAdminDto getById(Long id) {
        Club club = clubRepository.findByIdAndRecordStatusTrue(id)
                .orElseThrow(() -> new CustomExceptions("Club no encontrado", 404));
        return toDto(club);
    }

    @Override
    @Transactional
    public ClubAdminDto create(CreateClubRequestDto dto) {
        Club club = Club.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .capacity(dto.getCapacity())
                .build();

        if (dto.getReasonIds() != null && !dto.getReasonIds().isEmpty()) {
            Set<ClubReason> reasons = new HashSet<>(clubReasonRepository.findAllByIdInAndRecordStatusTrue(dto.getReasonIds()));
            club.setReasons(reasons);
        }
        if (dto.getInterestIds() != null && !dto.getInterestIds().isEmpty()) {
            Set<Interest> interests = new HashSet<>(interestRepository.findAllByIdInAndRecordStatusTrue(dto.getInterestIds()));
            club.setInterests(interests);
        }
        if (dto.getDesiredSoftSkillIds() != null && !dto.getDesiredSoftSkillIds().isEmpty()) {
            Set<SoftSkill> skills = new HashSet<>(softSkillRepository.findAllByIdInAndRecordStatusTrue(dto.getDesiredSoftSkillIds()));
            club.setDesiredSoftSkills(skills);
        }

        Club saved = clubRepository.save(club);
        return toDto(saved);
    }

    @Override
    @Transactional
    public ClubAdminDto update(Long id, UpdateClubRequestDto dto) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions("Club no encontrado", 404));

        if (dto.getName() != null) club.setName(dto.getName());
        if (dto.getDescription() != null) club.setDescription(dto.getDescription());
        if (dto.getCapacity() != null) club.setCapacity(dto.getCapacity());

        if (dto.getReasonIds() != null) {
            Set<ClubReason> reasons = new HashSet<>(clubReasonRepository.findAllByIdInAndRecordStatusTrue(dto.getReasonIds()));
            club.setReasons(reasons);
        }
        if (dto.getInterestIds() != null) {
            Set<Interest> interests = new HashSet<>(interestRepository.findAllByIdInAndRecordStatusTrue(dto.getInterestIds()));
            club.setInterests(interests);
        }
        if (dto.getDesiredSoftSkillIds() != null) {
            Set<SoftSkill> skills = new HashSet<>(softSkillRepository.findAllByIdInAndRecordStatusTrue(dto.getDesiredSoftSkillIds()));
            club.setDesiredSoftSkills(skills);
        }

        Club saved = clubRepository.save(club);
        return toDto(saved);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions("Club no encontrado", 404));
        clubRepository.delete(club);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClubMemberAdminDto> listMembers(Long clubId) {
        if (clubId == null) return List.of();
        List<ClubMember> members = clubMemberRepository.findAllByClubIdAndRecordStatusTrue(clubId);
        return members.stream().map(cm -> {
            var ui = cm.getUserInfo();
            String email = ui != null ? ui.getEmail() : null;
            String names = ui != null ? (ui.getNames() != null ? ui.getNames().trim() : "") : "";
            String surnames = ui != null ? (ui.getSurnames() != null ? ui.getSurnames().trim() : "") : "";
            String full = (names + " " + surnames).trim();
            if (full.isEmpty()) {
                if (ui != null && ui.getUsername() != null && !ui.getUsername().isBlank()) full = ui.getUsername();
                else if (email != null) full = email;
            }

            return ClubMemberAdminDto.builder()
                    .membershipId(cm.getId())
                    .studentId(cm.getStudent() != null ? cm.getStudent().getId() : null)
                    .userInfoId(ui != null ? ui.getId() : null)
                    .email(email)
                    .fullName(full)
                    .recordStatus(cm.getRecordStatus())
                    .joinedAt(cm.getCreatedAt())
                    .build();
        }).toList();
    }

    private ClubAdminDto toDto(Club club) {
        Set<Long> reasonIds = club.getReasons() != null
                ? club.getReasons().stream().map(ClubReason::getId).collect(Collectors.toSet())
                : Set.of();
        Set<String> reasonNames = club.getReasons() != null
                ? club.getReasons().stream().map(ClubReason::getName).collect(Collectors.toSet())
                : Set.of();

        Set<Long> interestIds = club.getInterests() != null
                ? club.getInterests().stream().map(Interest::getId).collect(Collectors.toSet())
                : Set.of();
        Set<String> interestNames = club.getInterests() != null
                ? club.getInterests().stream().map(Interest::getName).collect(Collectors.toSet())
                : Set.of();

        Set<Long> skillIds = club.getDesiredSoftSkills() != null
                ? club.getDesiredSoftSkills().stream().map(SoftSkill::getId).collect(Collectors.toSet())
                : Set.of();
        Set<String> skillNames = club.getDesiredSoftSkills() != null
                ? club.getDesiredSoftSkills().stream().map(SoftSkill::getName).collect(Collectors.toSet())
                : Set.of();

        return ClubAdminDto.builder()
                .id(club.getId())
                .name(club.getName())
                .description(club.getDescription())
                .capacity(club.getCapacity())
                .reasonIds(reasonIds)
                .reasonNames(reasonNames)
                .interestIds(interestIds)
                .interestNames(interestNames)
                .desiredSoftSkillIds(skillIds)
                .desiredSoftSkillNames(skillNames)
                .build();
    }
}