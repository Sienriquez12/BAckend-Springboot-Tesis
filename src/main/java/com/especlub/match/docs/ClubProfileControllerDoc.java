package com.especlub.match.docs;

import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.models.ClubProfile;
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

@Tag(name = "ClubProfile",
        description = "Endpoints públicos para consultar los perfiles de los clubes (usados por el motor de recomendación y la vista pública)")
public interface ClubProfileControllerDoc {

    @Operation(summary = "Listar perfiles activos para recomendación",
            description = "Devuelve la lista de perfiles de clubes marcados como activos para ser considerados por el motor de recomendación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfiles obtenidos correctamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ClubProfile.class))))
    })
    ResponseEntity<JsonDtoResponse<List<ClubProfile>>> listActiveForRecommendation();


    @Operation(summary = "Obtener perfil por id",
            description = "Obtiene un perfil de club por su id (solo si el club asociado está activo)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil encontrado",
                    content = @Content(schema = @Schema(implementation = ClubProfile.class))),
            @ApiResponse(responseCode = "404", description = "Perfil no encontrado", content = @Content)
    })
    ResponseEntity<JsonDtoResponse<ClubProfile>> getById(
            @Parameter(description = "ID del perfil de club", required = true) Long id
    );


    @Operation(summary = "Obtener perfil por id del club",
            description = "Obtiene el perfil asociado a un club dado su id (independiente del id del perfil).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil encontrado",
                    content = @Content(schema = @Schema(implementation = ClubProfile.class))),
            @ApiResponse(responseCode = "404", description = "Perfil no encontrado", content = @Content)
    })
    ResponseEntity<JsonDtoResponse<ClubProfile>> getByClubId(
            @Parameter(description = "ID del club", required = true) Long clubId
    );
}

