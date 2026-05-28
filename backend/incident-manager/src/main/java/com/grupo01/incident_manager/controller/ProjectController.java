package com.grupo01.incident_manager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupo01.incident_manager.dtos.project.ProjectCreateRequest;
import com.grupo01.incident_manager.dtos.project.ProjectResponse;
import com.grupo01.incident_manager.dtos.project.ProjectUpdateRequest;
import com.grupo01.incident_manager.service.ProjectService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> findAll() {
        return ResponseEntity.ok(projectService.findAll());
    }

    @GetMapping("/{idProject}")
    public ResponseEntity<ProjectResponse> findById(@PathVariable Long idProject) {
        return ResponseEntity.ok(projectService.findById(idProject));
    }

    @GetMapping("/key/{key}")
    public ResponseEntity<ProjectResponse> findByKey(@PathVariable String key) {
        return ResponseEntity.ok(projectService.findByKey(key));
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> create(@Valid @RequestBody ProjectCreateRequest request) {
        ProjectResponse createdProject = projectService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @PutMapping("/{idProject}")
    public ResponseEntity<ProjectResponse> update(
            @PathVariable Long idProject,
            @Valid @RequestBody ProjectUpdateRequest request
    ) {
        return ResponseEntity.ok(projectService.update(idProject, request));
    }

    @DeleteMapping("/{idProject}")
    public ResponseEntity<Void> delete(@PathVariable Long idProject) {
        projectService.delete(idProject);
        return ResponseEntity.noContent().build();
    }
}