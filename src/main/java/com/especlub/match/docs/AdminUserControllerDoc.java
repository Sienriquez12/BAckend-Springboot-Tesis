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

import com.especlub.match.dto.request.CreateUserRequestDto;
import com.especlub.match.dto.request.UpdateUserRequestDto;
import com.especlub.match.dto.request.UserAdminDto;
import com.especlub.match.dto.response.JsonDtoResponse;

@Tag(name = "Admin Users", description = "Operaciones CRUD administrativas sobre usuarios")
public interface AdminUserControllerDoc {

    @Operation(summary = "Listar usuarios", description = "Obtiene todos los usuarios activos (ámbito administrativo).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonDtoResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<JsonDtoResponse<List<UserAdminDto>>> listAll();

    @Operation(summary = "Obtener usuario por id", description = "Devuelve un usuario por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario obtenido",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "ID inválido"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<JsonDtoResponse<UserAdminDto>> getById(Long id);

    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario con la información enviada.")
    @RequestBody(description = "DTO con los datos para crear un usuario", required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserRequestDto.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<JsonDtoResponse<UserAdminDto>> create(CreateUserRequestDto dto);

    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente por ID.")
    @RequestBody(description = "DTO con los campos a actualizar", required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateUserRequestDto.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<JsonDtoResponse<UserAdminDto>> update(Long id, UpdateUserRequestDto dto);

    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario eliminado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonDtoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<JsonDtoResponse<Boolean>> delete(Long id);
}
