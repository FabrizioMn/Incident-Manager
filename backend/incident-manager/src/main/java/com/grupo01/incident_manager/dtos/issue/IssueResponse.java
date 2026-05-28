package com.grupo01.incident_manager.dtos.issue;

import java.time.LocalDateTime;

import com.grupo01.incident_manager.model.Issue;

public record IssueResponse(
        Long id,
        String ticketCode,
        String title,
        String description,
        String type,
        String status,
        String priority,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long idProject,
        String projectName,
        Long idCreator,
        String creatorName,
        Long idAssignee,
        String assigneeName
) {

    public static IssueResponse fromEntity(Issue issue) {
        return new IssueResponse(
                issue.getId(),
                issue.getTicketCode(),
                issue.getTitle(),
                issue.getDescription(),
                issue.getType(),
                issue.getStatus(),
                issue.getPriority(),
                issue.getCreatedAt(),
                issue.getUpdatedAt(),
                issue.getProject() != null ? issue.getProject().getId() : null,
                issue.getProject() != null ? issue.getProject().getName() : null,
                issue.getCreator() != null ? issue.getCreator().getId() : null,
                issue.getCreator() != null ? issue.getCreator().getName() : null,
                issue.getAssignee() != null ? issue.getAssignee().getId() : null,
                issue.getAssignee() != null ? issue.getAssignee().getName() : null
        );
    }
}