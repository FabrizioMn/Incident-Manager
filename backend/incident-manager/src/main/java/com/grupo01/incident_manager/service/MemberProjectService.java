package com.grupo01.incident_manager.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo01.incident_manager.dtos.memberproject.MemberProjectCreateRequest;
import com.grupo01.incident_manager.dtos.memberproject.MemberProjectResponse;
import com.grupo01.incident_manager.dtos.memberproject.MemberProjectUpdateRequest;
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

    @Transactional(readOnly = true)
    public List<MemberProjectResponse> findAll() {
        return memberProjectRepository.findAll()
                .stream()
                .map(MemberProjectResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public MemberProjectResponse findById(Long idProject, Long idUser) {
        MemberProject memberProject = findMemberProjectEntityById(idProject, idUser);
        return MemberProjectResponse.fromEntity(memberProject);
    }

    @Transactional(readOnly = true)
    public List<MemberProjectResponse> findMembersByProject(Long idProject) {
        validateProjectExists(idProject);

        return memberProjectRepository.findById_IdProject(idProject)
                .stream()
                .map(MemberProjectResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MemberProjectResponse> findProjectsByUser(Long idUser) {
        validateUserExists(idUser);

        return memberProjectRepository.findById_IdUser(idUser)
                .stream()
                .map(MemberProjectResponse::fromEntity)
                .toList();
    }

    @Transactional
    public MemberProjectResponse addMember(MemberProjectCreateRequest request) {
        Project project = projectRepository.findById(request.idProject())
                .orElseThrow(() -> new RuntimeException("No existe el proyecto con ID: " + request.idProject()));

        User user = userRepository.findById(request.idUser())
                .orElseThrow(() -> new RuntimeException("No existe el usuario con ID: " + request.idUser()));

        MemberProjectId id = new MemberProjectId(request.idProject(), request.idUser());

        if (memberProjectRepository.existsById(id)) {
            throw new RuntimeException("El usuario ya pertenece a este proyecto");
        }

        MemberProject memberProject = new MemberProject();
        memberProject.setId(id);
        memberProject.setProject(project);
        memberProject.setUser(user);
        memberProject.setProjectRol(normalizeProjectRol(request.projectRol()));

        MemberProject savedMemberProject = memberProjectRepository.save(memberProject);

        return MemberProjectResponse.fromEntity(savedMemberProject);
    }

    @Transactional
    public MemberProjectResponse updateMemberRole(
            Long idProject,
            Long idUser,
            MemberProjectUpdateRequest request
    ) {
        MemberProject memberProject = findMemberProjectEntityById(idProject, idUser);

        memberProject.setProjectRol(normalizeProjectRol(request.projectRol()));

        MemberProject updatedMemberProject = memberProjectRepository.save(memberProject);

        return MemberProjectResponse.fromEntity(updatedMemberProject);
    }

    @Transactional
    public void removeMember(Long idProject, Long idUser) {
        MemberProject memberProject = findMemberProjectEntityById(idProject, idUser);
        memberProjectRepository.delete(memberProject);
    }

    private MemberProject findMemberProjectEntityById(Long idProject, Long idUser) {
        MemberProjectId id = new MemberProjectId(idProject, idUser);

        return memberProjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "No existe relación entre el proyecto " + idProject + " y el usuario " + idUser
                ));
    }

    private void validateProjectExists(Long idProject) {
        if (!projectRepository.existsById(idProject)) {
            throw new RuntimeException("No existe el proyecto con ID: " + idProject);
        }
    }

    private void validateUserExists(Long idUser) {
        if (!userRepository.existsById(idUser)) {
            throw new RuntimeException("No existe el usuario con ID: " + idUser);
        }
    }

    private String normalizeProjectRol(String projectRol) {
        if (projectRol == null || projectRol.isBlank()) {
            throw new RuntimeException("El rol dentro del proyecto es obligatorio");
        }

        return projectRol.trim().toUpperCase();
    }
}