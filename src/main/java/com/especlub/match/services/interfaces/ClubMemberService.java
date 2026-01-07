package com.especlub.match.services.interfaces;

import java.util.List;
import java.util.Map;

public interface ClubMemberService {
    List<String> findActiveMemberEmailsByClubId(Long clubId);
    Map<String, String> findActiveMemberFullNamesByClubId(Long clubId);
}
