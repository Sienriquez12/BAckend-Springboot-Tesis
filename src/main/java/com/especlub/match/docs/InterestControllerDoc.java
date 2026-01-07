package com.especlub.match.docs;

import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.models.Interest;
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

@Tag(name = "Interests", description = "Operaciones para consultar intereses disponibles del sistema")
public interface InterestControllerDoc {

    @Operation(summary = "Listar intereses activos",
            description = "Devuelve todos los intereses que están activos en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Intereses obtenidos correctamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Interest.class))))
    })
    ResponseEntity<JsonDtoResponse<List<Interest>>> listAll();

    @Operation(summary = "Obtener interés por id",
            description = "Obtiene un interés por su identificador si existe y está activo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Interés encontrado",
                    content = @Content(schema = @Schema(implementation = Interest.class))),
            @ApiResponse(responseCode = "404", description = "Interés no encontrado", content = @Content)
    })
    ResponseEntity<JsonDtoResponse<Interest>> getById(
            @Parameter(description = "ID del interés", required = true) Long id
    );
}

