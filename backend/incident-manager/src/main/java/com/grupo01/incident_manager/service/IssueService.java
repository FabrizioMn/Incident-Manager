package com.grupo01.incident_manager.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo01.incident_manager.dtos.issue.IssueRequest;
import com.grupo01.incident_manager.dtos.issue.IssueResponse;
import com.grupo01.incident_manager.model.Issue;
import com.grupo01.incident_manager.model.Project;
import com.grupo01.incident_manager.model.User;
import com.grupo01.incident_manager.repository.IssueRepository;
import com.grupo01.incident_manager.repository.MemberProjectRepository;
import com.grupo01.incident_manager.repository.ProjectRepository;
import com.grupo01.incident_manager.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final MemberProjectRepository memberProjectRepository;

    @Transactional
    public IssueResponse createIssue(IssueRequest request) {
        // Buscamos el proyecto
        Project project = projectRepository.findById(request.idProject())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        // Buscamos al usuario creador
        User creator = userRepository.findById(request.idCreator())
                .orElseThrow(() -> new RuntimeException("Usuario creador no encontrado"));

        // Verificamos si la incidencia esta asignada a un usuario o no
        User assignee = null;
        if (request.idAssignee() != null) {
            // El asignado debe ser miembro activo del equipo del proyecto
            boolean isMember = memberProjectRepository.existsByProject_IdAndUser_Id(project.getId(),
                    request.idAssignee());
            if (!isMember) {
                throw new RuntimeException("No se puede asignar el ticket. El usuario no es miembro del proyecto.");
            }
            assignee = userRepository.findById(request.idAssignee()).orElse(null);
        }

        // Logica para crera el ticker code y contar las incidencias existentes del
        // proyecto
        long currentCount = issueRepository.countByProject_Id(project.getId());
        String generatedCode = project.getKey() + "-" + (currentCount + 1);

        // Construimos la entidad issue
        Issue issue = Issue.builder()
                .ticketCode(generatedCode)
                .title(request.title())
                .description(request.description())
                .type(request.type().trim().toUpperCase())
                .priority(request.priority().trim().toUpperCase())
                .status("BACKLOG") // Estado por defecto
                .project(project)
                .creator(creator)
                .assignee(assignee)
                .updatedAt(LocalDateTime.now())
                .build();

        Issue savedIssue = issueRepository.save(issue);
        return mapToResponse(savedIssue);

    }

    @Transactional(readOnly = true)
    public List<IssueResponse> getIssueByProject(Long idProject) {
        return issueRepository.findByProject_Id(idProject).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public IssueResponse updateIssueStatus(Long idIssue, String newStatus) {
        Issue issue = issueRepository.findById(idIssue)
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada."));

        issue.setStatus(newStatus.trim().toUpperCase());
        return mapToResponse(issueRepository.save(issue));
    }

    private IssueResponse mapToResponse(Issue issue) {
        return new IssueResponse(
                issue.getId(),
                issue.getTicketCode(),
                issue.getTitle(),
                issue.getDescription(),
                issue.getType(),
                issue.getStatus(),
                issue.getPriority(),
                issue.getProject().getId(),
                issue.getProject().getKey(),
                issue.getCreator().getName(),
                issue.getAssignee() != null ? issue.getAssignee().getName() : "Unassigned",
                issue.getCreatedAt(),
                issue.getUpdatedAt());
    }
}
