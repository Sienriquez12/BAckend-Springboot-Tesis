package com.especlub.match.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import com.especlub.match.dto.response.JsonDtoResponse;

@Tag(name = "Student Clubs", description = "Operaciones para que estudiantes se inscriban o salgan de clubs")
public interface StudentClubControllerDoc {

    @Operation(summary = "Inscribir estudiante a un club", description = "Enrola a un estudiante en un club y devuelve el enlace de contacto (por ejemplo WhatsApp).")
    @Parameter(name = "clubId", description = "ID del club", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inscripción realizada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Club o estudiante no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<JsonDtoResponse<String>> enroll(Long clubId, HttpServletRequest request);

    @Operation(summary = "Retirar estudiante de un club", description = "Permite que un estudiante deje un club")
    @Parameter(name = "clubId", description = "ID del club", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Salida realizada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Club o estudiante no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<JsonDtoResponse<Void>> leave(Long clubId, HttpServletRequest request);
}