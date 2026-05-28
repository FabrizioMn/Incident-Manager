package com.grupo01.incident_manager.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo01.incident_manager.dtos.project.ProjectCreateRequest;
import com.grupo01.incident_manager.dtos.project.ProjectResponse;
import com.grupo01.incident_manager.dtos.project.ProjectUpdateRequest;
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

    @Transactional(readOnly = true)
    public List<ProjectResponse> findAll() {
        return projectRepository.findAll()
                .stream()
                .map(ProjectResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectResponse findById(Long idProject) {
        Project project = findProjectEntityById(idProject);
        return ProjectResponse.fromEntity(project);
    }

    @Transactional(readOnly = true)
    public ProjectResponse findByKey(String key) {
        Project project = projectRepository.findByKey(key)
                .orElseThrow(() -> new RuntimeException("No existe un proyecto con la clave: " + key));

        return ProjectResponse.fromEntity(project);
    }

    @Transactional
    public ProjectResponse create(ProjectCreateRequest request) {
        String normalizedKey = normalizeKey(request.key());

        if (projectRepository.existsByKey(normalizedKey)) {
            throw new RuntimeException("Ya existe un proyecto con la clave: " + normalizedKey);
        }

        User author = userRepository.findById(request.idAuthor())
                .orElseThrow(() -> new RuntimeException("No existe el usuario autor con ID: " + request.idAuthor()));

        IssueScheme issueScheme = issueSchemeRepository.findById(request.idIssueScheme())
                .orElseThrow(() -> new RuntimeException("No existe el esquema de issues con ID: " + request.idIssueScheme()));

        Project project = new Project();
        project.setName(request.name());
        project.setKey(normalizedKey);
        project.setDescription(request.description());
        project.setAuthor(author);
        project.setScheme(issueScheme);

        Project savedProject = projectRepository.save(project);

        return ProjectResponse.fromEntity(savedProject);
    }

    @Transactional
    public ProjectResponse update(Long idProject, ProjectUpdateRequest request) {
        Project project = findProjectEntityById(idProject);

        String normalizedKey = normalizeKey(request.key());

        boolean keyBelongsToAnotherProject = projectRepository.findByKey(normalizedKey)
                .filter(existingProject -> !existingProject.getId().equals(idProject))
                .isPresent();

        if (keyBelongsToAnotherProject) {
            throw new RuntimeException("Ya existe otro proyecto con la clave: " + normalizedKey);
        }

        IssueScheme issueScheme = issueSchemeRepository.findById(request.idIssueScheme())
                .orElseThrow(() -> new RuntimeException("No existe el esquema de issues con ID: " + request.idIssueScheme()));

        project.setName(request.name());
        project.setKey(normalizedKey);
        project.setDescription(request.description());
        project.setScheme(issueScheme);

        Project updatedProject = projectRepository.save(project);

        return ProjectResponse.fromEntity(updatedProject);
    }

    @Transactional
    public void delete(Long idProject) {
        Project project = findProjectEntityById(idProject);

        try {
            projectRepository.delete(project);
        } catch (DataIntegrityViolationException exception) {
            throw new RuntimeException(
                    "No se puede eliminar el proyecto porque tiene información relacionada, como issues o miembros."
            );
        }
    }

    private Project findProjectEntityById(Long idProject) {
        return projectRepository.findById(idProject)
                .orElseThrow(() -> new RuntimeException("No existe el proyecto con ID: " + idProject));
    }

    private String normalizeKey(String key) {
        if (key == null || key.isBlank()) {
            throw new RuntimeException("La clave del proyecto es obligatoria");
        }

        return key.trim().toUpperCase();
    }
}