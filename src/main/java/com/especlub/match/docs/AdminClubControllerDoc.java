package com.especlub.match.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.http.ResponseEntity;

import java.util.List;

import com.especlub.match.dto.request.CreateClubRequestDto;
import com.especlub.match.dto.request.UpdateClubRequestDto;
import com.especlub.match.dto.response.ClubAdminDto;
import com.especlub.match.dto.response.JsonDtoResponse;

@Tag(name = "Admin Clubs", description = "Operaciones CRUD para administración de clubs")
public interface AdminClubControllerDoc {

    @Operation(summary = "Listar clubs", description = "Obtiene todos los clubs (ámbito administrativo).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de clubs obtenida",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonDtoResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<JsonDtoResponse<List<ClubAdminDto>>> listAll();

    @Operation(summary = "Obtener club por id", description = "Devuelve un club por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Club obtenido",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "ID inválido"),
            @ApiResponse(responseCode = "404", description = "Club no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<JsonDtoResponse<ClubAdminDto>> getById(Long id);

    @Operation(summary = "Listar miembros de un club", description = "Obtiene los miembros (estudiantes) activos de un club por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Miembros obtenidos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "ID inválido"),
            @ApiResponse(responseCode = "404", description = "Club no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<JsonDtoResponse<List<com.especlub.match.dto.response.ClubMemberAdminDto>>> listMembers(Long id);

    @Operation(summary = "Crear club", description = "Crea un nuevo club con la información enviada.")
    @RequestBody(description = "DTO con los datos para crear un club", required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateClubRequestDto.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Club creado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<JsonDtoResponse<ClubAdminDto>> create(CreateClubRequestDto dto);

    @Operation(summary = "Actualizar club", description = "Actualiza los datos de un club existente por ID.")
    @RequestBody(description = "DTO con los campos a actualizar", required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateClubRequestDto.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Club actualizado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Club no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<JsonDtoResponse<ClubAdminDto>> update(Long id, UpdateClubRequestDto dto);

    @Operation(summary = "Eliminar club", description = "Elimina un club por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Club eliminado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonDtoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Club no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<JsonDtoResponse<Boolean>> delete(Long id);
}
