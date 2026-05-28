package com.grupo01.incident_manager.dtos.memberproject;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberProjectUpdateRequest(

        @NotBlank(message = "El rol dentro del proyecto es obligatorio")
        @Size(max = 50, message = "El rol dentro del proyecto no puede superar los 50 caracteres")
        String projectRol
) {
}