package com.especlub.match.docs;

import com.especlub.match.dto.request.PasswordUpdateInternalRequestDto;
import com.especlub.match.dto.response.AdminUserSummaryDto;
import com.especlub.match.dto.response.EventAdminDto;
import com.especlub.match.dto.response.JsonDtoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(
        name = "Métodos para el Usuario",
        description = "Operaciones relacionadas con la administración del usuario autenticado: obtener información, actualizar contraseña y consultar eventos activos."
)
public interface UserControllerDoc {

    // ============================================================
    // GET /me
    // ============================================================
    @Operation(
            summary = "Obtener usuario actual",
            description = "Obtiene la información del usuario autenticado mediante el JWT presente en el header o cookie."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario obtenido correctamente",
                    content = @Content(schema = @Schema(implementation = AdminUserSummaryDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content)
    })
    ResponseEntity<JsonDtoResponse<AdminUserSummaryDto>> getCurrentUser(
            @Parameter(hidden = true) HttpServletRequest request
    );

    // ============================================================
    // PATCH /internal/update-password
    // ============================================================
    @Operation(
            summary = "Actualizar contraseña interna",
            description = "Permite actualizar la contraseña del usuario autenticado. El JWT se obtiene del header o de la cookie."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contraseña actualizada correctamente",
                    content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content)
    })
    ResponseEntity<JsonDtoResponse<Boolean>> updatePasswordInternal(
            @Parameter(description = "Datos para actualizar la contraseña", required = true)
            @Valid PasswordUpdateInternalRequestDto dto,
            @Parameter(hidden = true) HttpServletRequest request
    );

    // ============================================================
    // GET /notifications/events/active
    // ============================================================
    @Operation(
            summary = "Obtener eventos activos del usuario",
            description = "Retorna la lista de eventos activos asociados al usuario autenticado. El JWT se obtiene del header o cookie."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Eventos activos obtenidos correctamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = EventAdminDto.class)))),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content)
    })
    ResponseEntity<JsonDtoResponse<List<EventAdminDto>>> getAllActive(
            @Parameter(hidden = true) HttpServletRequest request
    );
}
