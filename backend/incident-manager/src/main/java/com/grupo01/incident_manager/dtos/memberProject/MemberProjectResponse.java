package com.grupo01.incident_manager.dtos.memberProject;

public record MemberProjectResponse(
        Long idProject,
        String projectName,
        Long idUser,
        String userName,
        String userEmail,
        Long idRole,
        String roleName) {

}
