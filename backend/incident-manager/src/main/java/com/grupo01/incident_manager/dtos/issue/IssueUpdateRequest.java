package com.grupo01.incident_manager.dtos.issue;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IssueUpdateRequest(

        @NotBlank(message = "El título es obligatorio")
        @Size(max = 150, message = "El título no puede superar los 150 caracteres")
        String title,

        String description,

        @NotBlank(message = "El tipo es obligatorio")
        @Size(max = 50, message = "El tipo no puede superar los 50 caracteres")
        String type,

        @NotBlank(message = "El estado es obligatorio")
        @Size(max = 50, message = "El estado no puede superar los 50 caracteres")
        String status,

        @NotBlank(message = "La prioridad es obligatoria")
        @Size(max = 50, message = "La prioridad no puede superar los 50 caracteres")
        String priority,

        Long idAssignee
) {
}