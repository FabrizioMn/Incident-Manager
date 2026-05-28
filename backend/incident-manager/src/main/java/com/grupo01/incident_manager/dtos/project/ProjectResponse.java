package com.grupo01.incident_manager.dtos.project;

import java.time.LocalDateTime;

import com.grupo01.incident_manager.model.Project;

public record ProjectResponse(
        Long id,
        String name,
        String key,
        String description,
        LocalDateTime createdAt,
        Long idAuthor,
        String authorName,
        Long idIssueScheme,
        String issueSchemeName
) {

    public static ProjectResponse fromEntity(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getKey(),
                project.getDescription(),
                project.getCreatedAt(),
                project.getAuthor() != null ? project.getAuthor().getId() : null,
                project.getAuthor() != null ? project.getAuthor().getName() : null,
                project.getScheme() != null ? project.getScheme().getId() : null,
                project.getScheme() != null ? project.getScheme().getName() : null
        );
    }
}