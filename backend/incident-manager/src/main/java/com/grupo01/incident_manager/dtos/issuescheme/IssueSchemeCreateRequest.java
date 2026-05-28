package com.grupo01.incident_manager.dtos.issuescheme;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IssueSchemeCreateRequest(

        @NotBlank(message = "El nombre del esquema es obligatorio")
        @Size(max = 100, message = "El nombre del esquema no puede superar los 100 caracteres")
        String name,

        @NotBlank(message = "La descripción del esquema es obligatoria")
        @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
        String description
) {
}