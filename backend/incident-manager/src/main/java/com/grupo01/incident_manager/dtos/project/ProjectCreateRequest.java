package com.grupo01.incident_manager.dtos.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProjectCreateRequest(

        @NotBlank(message = "El nombre del proyecto es obligatorio")
        @Size(max = 100, message = "El nombre del proyecto no puede superar los 100 caracteres")
        String name,

        @NotBlank(message = "La clave del proyecto es obligatoria")
        @Size(max = 10, message = "La clave del proyecto no puede superar los 10 caracteres")
        String key,

        @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
        String description,

        @NotNull(message = "El autor del proyecto es obligatorio")
        Long idAuthor,

        @NotNull(message = "El esquema de issues es obligatorio")
        Long idIssueScheme
) {
}