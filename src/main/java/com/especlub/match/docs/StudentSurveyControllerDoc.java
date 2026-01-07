package com.especlub.match.docs;

import com.especlub.match.dto.request.CreateSurveyRequestDto;
import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.dto.response.RecommendationListDto;
import com.especlub.match.dto.response.StudentSurveyResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "Student Survey API", description = "Endpoints para crear encuestas de estudiantes y generar recomendaciones de clubes")
public interface StudentSurveyControllerDoc {

    @Operation(summary = "Crear encuesta del estudiante",
            description = "Guarda la información de la encuesta del estudiante, relaciones derivadas (intereses, soft skills, motivos) y preferencias."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Encuesta creada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentSurveyResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error del servidor")
    })
    ResponseEntity<JsonDtoResponse<StudentSurveyResponseDto>> createSurvey(
            HttpServletRequest request,
            @RequestBody(description = "Payload con las respuestas/ids y preferencias del estudiante") CreateSurveyRequestDto dto
    );

    @Operation(summary = "Generar recomendaciones de clubes",
            description = "Lanza la generación de recomendación (LLM) y devuelve una lista ordenada de clubes con puntajes y razones.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recomendaciones generadas",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecommendationListDto.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error del servidor")
    })
    ResponseEntity<JsonDtoResponse<RecommendationListDto>> generateRecommendation(HttpServletRequest request);
}
