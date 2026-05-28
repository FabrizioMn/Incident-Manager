package com.grupo01.incident_manager.dtos.role;

import com.grupo01.incident_manager.model.Role;

public record RoleResponse(
        Long id,
        String name,
        String description
) {

    public static RoleResponse fromEntity(Role role) {
        return new RoleResponse(
                role.getId(),
                role.getName(),
                role.getDescription()
        );
    }
}