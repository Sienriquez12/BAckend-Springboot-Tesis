package com.especlub.match.clients.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExternalRecommendationResponseDto {
    private boolean error;
    private EstudianteDto estudiante;
    private List<RecomendacionDto> recomendaciones;
    private MetadataDto metadata;
    private String mensaje;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EstudianteDto {
        private Long id;
        private String nombre;
        private String carrera;
        private Integer semestre;
        private List<String> intereses;

        @JsonProperty("grupo_cluster")
        private Integer grupoCluster;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecomendacionDto {
        @JsonProperty("club_id")
        private Long clubId;

        @JsonProperty("club_name")
        private String clubName;

        @JsonProperty("club_type")
        private String clubType;

        private Double afinidad;

        @JsonProperty("afinidad_porcentaje")
        private Double afinidadPorcentaje;

        @JsonProperty("capacidad_disponible")
        private Integer capacidadDisponible;

        @JsonProperty("horas_semanales")
        private Integer horasSemanales;

        private String descripcion;

        @JsonProperty("razones_match")
        private List<String> razonesMatch;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MetadataDto {
        @JsonProperty("total_clubes_evaluados")
        private Integer totalClubesEvaluados;

        @JsonProperty("tiempo_procesamiento_segundos")
        private Double tiempoProcesamientoSegundos;

        private String algoritmo;
    }
}
