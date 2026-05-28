package com.grupo01.incident_manager.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo01.incident_manager.dtos.memberProject.MemberProjectRequest;
import com.grupo01.incident_manager.dtos.memberProject.MemberProjectResponse;
import com.grupo01.incident_manager.model.MemberProject;
import com.grupo01.incident_manager.model.MemberProjectId;
import com.grupo01.incident_manager.model.Project;
import com.grupo01.incident_manager.model.User;
import com.grupo01.incident_manager.repository.MemberProjectRepository;
import com.grupo01.incident_manager.repository.ProjectRepository;
import com.grupo01.incident_manager.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberProjectService {

    private final MemberProjectRepository memberProjectRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional
    public MemberProjectResponse addMemberToProject(MemberProjectRequest request) {

        // Verificamos si el miembor ya pertenece al proyecto
        if (memberProjectRepository.existsByProject_IdAndUser_Id(request.idProject(), request.idUser())) {
            throw new RuntimeException("El usuario ya es miembro de este proyecto");
        }

        // Buscamos las entidades correspondientes
        Project project = projectRepository.findById(request.idProject())
                .orElseThrow(
                        () -> new RuntimeException("Proyecto no encontrado."));

        User user = userRepository.findById(request.idUser())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        // Creamos el ID compuesto
        MemberProjectId idCompound = new MemberProjectId(request.idProject(), request.idUser());

        // Contruimos la entidad
        MemberProject memberProject = MemberProject.builder()
                .id(idCompound)
                .project(project)
                .user(user)
                .projectRol(request.projectRole().trim().toUpperCase())
                .build();
        MemberProject savedMember = memberProjectRepository.save(memberProject);
        return mapToResponse(savedMember);

    }

    private MemberProjectResponse mapToResponse(MemberProject member) {
        return new MemberProjectResponse(
                member.getProject().getId(),
                member.getProject().getName(),
                member.getUser().getId(),
                member.getUser().getName(),
                member.getUser().getEmail(),
                member.getProjectRol(),
                member.getJoinDate());
    }
}
