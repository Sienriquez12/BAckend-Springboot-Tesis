package com.especlub.match.services.impl;
import com.especlub.match.models.ClubMember;
import com.especlub.match.repositories.ClubMemberRepository;
import com.especlub.match.services.interfaces.ClubMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubMemberServiceImpl implements ClubMemberService {

    private final ClubMemberRepository clubMemberRepository;

    @Override
    public List<String> findActiveMemberEmailsByClubId(Long clubId) {
        if (clubId == null) {
            return Collections.emptyList();
        }

        List<ClubMember> members = clubMemberRepository.findAllByClubIdAndRecordStatusTrue(clubId);
        if (members == null || members.isEmpty()) {
            return Collections.emptyList();
        }

        return members.stream()
                .map(this::extractEmail)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(email -> !email.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }

    private String extractEmail(ClubMember member) {
        if (member == null) return null;
        if (member.getUserInfo() != null && member.getUserInfo().getEmail() != null) {
            return member.getUserInfo().getEmail();
        }
        return null;
    }

    @Override
    public Map<String, String> findActiveMemberFullNamesByClubId(Long clubId) {
        List<ClubMember> members = clubMemberRepository.findAllByClubIdAndRecordStatusTrue(clubId);
        Map<String, String> result = new LinkedHashMap<>();
        for (ClubMember cm : members) {
            if (cm == null || cm.getStudent() == null || cm.getStudent().getUserInfo() == null) continue;
            var ui = cm.getStudent().getUserInfo();
            String email = ui.getEmail();
            if (email == null || email.isBlank()) continue;

            String first = ui.getNames() != null ? ui.getNames().trim() : "";
            String last = ui.getSurnames() != null ? ui.getSurnames().trim() : "";
            String full = (first + " " + last).trim();

            if (full.isEmpty()) {
                if (ui.getUsername() != null && !ui.getUsername().isBlank()) full = ui.getUsername();
                else full = email;
            }

            result.put(email, full);
        }
        return result;
    }
}