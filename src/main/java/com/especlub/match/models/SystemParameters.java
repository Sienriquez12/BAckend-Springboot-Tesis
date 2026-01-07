package com.especlub.match.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;


@Entity
@Table(name = "system_parameters")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemParameters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("Identificador único del parámetro del sistema.")
    private Long id;

    @Column(name = "mnemonic")
    @Comment("Código mnemónico del parámetro.")
    private String mnemonic;


    @Column(name = "name")
    @Comment("Nombre del parámetro.")
    private String name;

    @Column(name = "description")
    @Comment("Descripción del parámetro.")
    private String description;


    @Column(name = "value")
    @Comment("Valor del parámetro.")
    private String value;


    @Column(name = "record_status")
    @Comment("Estado del registro (activo/inactivo).")
    private Boolean recordStatus;


    @Column(name = "created_at")
    @Comment("Fecha de creación.")
    private LocalDateTime createdAt;


    @Column(name = "updated_at")
    @Comment("Fecha de última actualización.")
    private LocalDateTime updatedAt;
}
