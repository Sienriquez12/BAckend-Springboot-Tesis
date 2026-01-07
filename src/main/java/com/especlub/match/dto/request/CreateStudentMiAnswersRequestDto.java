package com.especlub.match.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateStudentMiAnswersRequestDto {
    private List<MiAnswerDto> answers;

    @Data
    public static class MiAnswerDto {
        private Long questionId;
        private Integer score; // e.g., 1..5
    }
}
