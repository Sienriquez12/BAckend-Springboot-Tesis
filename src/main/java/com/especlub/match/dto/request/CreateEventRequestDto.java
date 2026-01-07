package com.especlub.match.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEventRequestDto {
    @NotBlank(message = "El titulo es obligatorio")
    private String title;

    private String description;

    @NotNull(message = "La fecha y hora de inicio es obligatoria")
    private LocalDateTime startAt;

    @NotNull(message = "La fecha y hora de fin es obligatoria")
    private LocalDateTime endAt;

    private String location;

    private String virtualLink;

    @NotNull(message = "El ID del club es obligatorio")
    private Long clubId;

    @NotNull(message = "El ID del usuario que crea el evento es obligatorio")
    private Long createdByUserInfoId;
}