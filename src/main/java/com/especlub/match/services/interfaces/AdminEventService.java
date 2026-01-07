package com.especlub.match.services.interfaces;

import com.especlub.match.dto.request.CreateEventRequestDto;
import com.especlub.match.dto.request.UpdateEventRequestDto;
import com.especlub.match.dto.response.EventAdminDto;

import java.util.List;

public interface AdminEventService {
    EventAdminDto create(CreateEventRequestDto dto);
    EventAdminDto findById(Long id);
    List<EventAdminDto> findAllActiveByUser(String jwt);
    List<EventAdminDto> findAllActive();
    EventAdminDto update(Long id, UpdateEventRequestDto dto);
}