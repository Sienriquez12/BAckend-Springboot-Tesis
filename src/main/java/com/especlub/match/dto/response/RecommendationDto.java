package com.especlub.match.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecommendationDto {
    private Long clubId;
    private String clubName;
    private Double score;
    private String reason; // short explanation why recommended
}

