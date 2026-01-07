package com.especlub.match.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StudentMiResultDto {
    private Long studentId;
    private List<IntelligenceScore> scores;

    @Data
    @Builder
    public static class IntelligenceScore {
        private Long intelligenceTypeId;
        private String code;
        private String name;
        private Integer totalScore;
        private Double normalized; // 0..1
    }
}

