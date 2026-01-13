package com.especlub.match.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClubTypeDto {
    private Long id;
    private String name;
    private String description;
    private String icon;
    private String color;
    private Integer orderIndex;
}
