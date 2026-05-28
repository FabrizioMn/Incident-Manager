package com.grupo01.incident_manager.dtos.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RoleCreateRequest(

        @NotBlank(message = "El nombre del rol es obligatorio")
        @Size(max = 50, message = "El nombre del rol no puede superar los 50 caracteres")
        String name,

        @Size(max = 300, message = "La descripción no puede superar los 300 caracteres")
        String description
) {
}