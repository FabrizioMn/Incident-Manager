package com.grupo01.incident_manager.dtos.memberproject;

import java.time.LocalDateTime;

import com.grupo01.incident_manager.model.MemberProject;

public record MemberProjectResponse(
        Long idProject,
        String projectName,
        String projectKey,
        Long idUser,
        String userName,
        String userEmail,
        String projectRol,
        LocalDateTime joinDate
) {

    public static MemberProjectResponse fromEntity(MemberProject memberProject) {
        return new MemberProjectResponse(
                memberProject.getProject() != null ? memberProject.getProject().getId() : null,
                memberProject.getProject() != null ? memberProject.getProject().getName() : null,
                memberProject.getProject() != null ? memberProject.getProject().getKey() : null,
                memberProject.getUser() != null ? memberProject.getUser().getId() : null,
                memberProject.getUser() != null ? memberProject.getUser().getName() : null,
                memberProject.getUser() != null ? memberProject.getUser().getEmail() : null,
                memberProject.getProjectRol(),
                memberProject.getJoinDate()
        );
    }
}