package com.grupo01.incident_manager.dtos.user;

import java.time.LocalDateTime;

import com.grupo01.incident_manager.model.User;

public record UserResponse(
        Long id,
        String name,
        String email,
        LocalDateTime createdAt,
        Long idRole,
        String roleName
) {

    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getRole() != null ? user.getRole().getId() : null,
                user.getRole() != null ? user.getRole().getName() : null
        );
    }
}