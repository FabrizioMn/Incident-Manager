package com.grupo01.incident_manager.dtos.issue;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IssueStatusUpdateRequest(

        @NotBlank(message = "El estado es obligatorio")
        @Size(max = 50, message = "El estado no puede superar los 50 caracteres")
        String status
) {
}