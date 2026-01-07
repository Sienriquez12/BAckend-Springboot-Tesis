package com.especlub.match.docs;

import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.models.SoftSkill;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "SoftSkills", description = "Operaciones para consultar habilidades blandas disponibles en el sistema")
public interface SoftSkillControllerDoc {

    @Operation(summary = "Listar habilidades blandas activas",
            description = "Devuelve todas las soft skills que están activas en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Soft skills obtenidas correctamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SoftSkill.class))))
    })
    ResponseEntity<JsonDtoResponse<List<SoftSkill>>> listAll();

    @Operation(summary = "Obtener habilidad blanda por id",
            description = "Obtiene una soft skill por su identificador si existe y está activa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Soft skill encontrada",
                    content = @Content(schema = @Schema(implementation = SoftSkill.class))),
            @ApiResponse(responseCode = "404", description = "Soft skill no encontrada", content = @Content)
    })
    ResponseEntity<JsonDtoResponse<SoftSkill>> getById(
            @Parameter(description = "ID de la soft skill", required = true) Long id
    );
}

