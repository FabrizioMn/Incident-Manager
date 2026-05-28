package com.grupo01.incident_manager.service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo01.incident_manager.dtos.project.ProjectRequest;
import com.grupo01.incident_manager.dtos.project.ProjectResponse;
import com.grupo01.incident_manager.model.IssueScheme;
import com.grupo01.incident_manager.model.Project;
import com.grupo01.incident_manager.model.User;
import com.grupo01.incident_manager.repository.IssueSchemeRepository;
import com.grupo01.incident_manager.repository.ProjectRepository;
import com.grupo01.incident_manager.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final IssueSchemeRepository issueSchemeRepository;

    @Transactional
    public ProjectResponse createProject(ProjectRequest request) {

        String formattedKey = request.key().trim().toUpperCase();

        // Verificamos que no exista otro projecto con la misma KEY
        if (projectRepository.existsByKey(formattedKey)) {
            throw new RuntimeException("Ya existe un proyecto con la KEY: " + formattedKey);
        }

        // Buscamos al usuario en la BD
        User creator = userRepository.findById(request.idUser())
                .orElseThrow(() -> new RuntimeException("Usuario creador no encontrado"));

        // Buscamos el esquema de incidencias en la BD
        IssueScheme scheme = issueSchemeRepository.findById(request.idIssueScheme())
                .orElseThrow(() -> new RuntimeException("Esquema de incidencias no encontrado"));

        // Contruimos el proyecto
        Project project = Project.builder()
                .name(request.name().trim())
                .key(formattedKey)
                .description(request.description())
                .author(creator)
                .scheme(scheme)
                .build();

        Project savedProject = projectRepository.save(project);
        return mapToResponse(savedProject);

    }

    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ProjectResponse getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        return mapToResponse(project);
    }

    public void deleteProject(Long id) {
        // Verificamos si el proyecto existe
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("El proyecto no existe");
        }
        // Eliminamos el proyecto de la base de datos
        projectRepository.deleteById(id);
    }

    private ProjectResponse mapToResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getKey(),
                project.getDescription(),
                project.getScheme().getId(),
                project.getScheme().getName(),
                project.getAuthor().getId(),
                project.getAuthor().getName(),
                project.getCreatedAt());
    }
}
