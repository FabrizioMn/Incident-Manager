package com.grupo01.incident_manager.dtos.user;

import jakarta.validation.constraints.NotNull;

public record UserRoleUpdateRequest(

        @NotNull(message = "El rol es obligatorio")
        Long idRole
) {
}