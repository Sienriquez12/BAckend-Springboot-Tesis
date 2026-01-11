package com.especlub.match.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.http.ResponseEntity;

import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.models.UserRole;

import java.util.List;

@Tag(name = "Admin Roles", description = "Operaciones de administración para gestión de roles")
public interface AdminRoleControllerDoc {

    @Operation(summary = "Listar todos los roles activos", description = "Obtiene una lista de todos los roles activos en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Roles obtenidos exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonDtoResponse.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<JsonDtoResponse<List<UserRole>>> listAllActive();

    @Operation(summary = "Obtener rol por ID", description = "Obtiene un rol específico por su identificador")
    @Parameter(name = "id", description = "ID del rol", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rol obtenido exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonDtoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado"),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<JsonDtoResponse<UserRole>> getById(Long id);
}
