package com.especlub.match.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClubSummaryDto {
    private Long id;
    private String name;
    private Boolean recordStatus;
}

