package com.especlub.match.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.http.ResponseEntity;

import java.util.List;

import com.especlub.match.dto.response.ClubAdminDto;
import com.especlub.match.dto.response.JsonDtoResponse;

@Tag(name = "Clubs", description = "Operaciones públicas para consultar clubs")
public interface ClubControllerDoc {

    @Operation(summary = "Listar clubs", description = "Obtiene todos los clubs disponibles.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de clubs obtenida",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonDtoResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<JsonDtoResponse<List<ClubAdminDto>>> listAll();

    @Operation(summary = "Obtener club por id", description = "Devuelve un club por su ID.")
    @Parameter(name = "id", description = "ID del club", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Club obtenido",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "ID inválido"),
            @ApiResponse(responseCode = "404", description = "Club no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<JsonDtoResponse<ClubAdminDto>> getById(Long id);
}

