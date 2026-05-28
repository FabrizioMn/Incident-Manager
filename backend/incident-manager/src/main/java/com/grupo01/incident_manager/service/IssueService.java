package com.grupo01.incident_manager.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo01.incident_manager.dtos.issue.IssueCreateRequest;
import com.grupo01.incident_manager.dtos.issue.IssueResponse;
import com.grupo01.incident_manager.dtos.issue.IssueUpdateRequest;
import com.grupo01.incident_manager.model.Issue;
import com.grupo01.incident_manager.model.Project;
import com.grupo01.incident_manager.model.User;
import com.grupo01.incident_manager.repository.IssueRepository;
import com.grupo01.incident_manager.repository.ProjectRepository;
import com.grupo01.incident_manager.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<IssueResponse> findAll() {
        return issueRepository.findAll()
                .stream()
                .map(IssueResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public IssueResponse findById(Long idIssue) {
        Issue issue = findIssueEntityById(idIssue);
        return IssueResponse.fromEntity(issue);
    }

    @Transactional(readOnly = true)
    public IssueResponse findByTicketCode(String ticketCode) {
        Issue issue = issueRepository.findByTicketCode(ticketCode)
                .orElseThrow(() -> new RuntimeException("No existe un ticket con el código: " + ticketCode));

        return IssueResponse.fromEntity(issue);
    }

    @Transactional(readOnly = true)
    public List<IssueResponse> findByProject(Long idProject) {
        validateProjectExists(idProject);

        return issueRepository.findByProject_Id(idProject)
                .stream()
                .map(IssueResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<IssueResponse> findByProjectAndStatus(Long idProject, String status) {
        validateProjectExists(idProject);

        return issueRepository.findByProject_IdAndStatus(idProject, status)
                .stream()
                .map(IssueResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<IssueResponse> findAssignedOpenIssues(Long idUser) {
        validateUserExists(idUser);

        return issueRepository.findByAssignee_IdAndStatusNot(idUser, "CERRADO")
                .stream()
                .map(IssueResponse::fromEntity)
                .toList();
    }

    @Transactional
    public IssueResponse create(IssueCreateRequest request) {
        boolean ticketCodeAlreadyExists = issueRepository.findByTicketCode(request.ticketCode()).isPresent();

        if (ticketCodeAlreadyExists) {
            throw new RuntimeException("Ya existe un ticket con el código: " + request.ticketCode());
        }

        Project project = projectRepository.findById(request.idProject())
                .orElseThrow(() -> new RuntimeException("No existe el proyecto con ID: " + request.idProject()));

        User creator = userRepository.findById(request.idCreator())
                .orElseThrow(() -> new RuntimeException("No existe el usuario creador con ID: " + request.idCreator()));

        User assignee = null;

        if (request.idAssignee() != null) {
            assignee = userRepository.findById(request.idAssignee())
                    .orElseThrow(() -> new RuntimeException("No existe el usuario asignado con ID: " + request.idAssignee()));
        }

        LocalDateTime now = LocalDateTime.now();

        Issue issue = new Issue();
        issue.setTicketCode(request.ticketCode());
        issue.setTitle(request.title());
        issue.setDescription(request.description());
        issue.setType(request.type());
        issue.setStatus(request.status());
        issue.setPriority(request.priority());
        issue.setProject(project);
        issue.setCreator(creator);
        issue.setAssignee(assignee);

        /*
         * Esto evita tocar Issue.java.
         * Tu entidad tiene updatedAt como nullable = false,
         * pero su @PrePersist solo llena createdAt.
         * Por eso llenamos ambos campos desde el servicio.
         */
        issue.setCreatedAt(now);
        issue.setUpdatedAt(now);

        Issue savedIssue = issueRepository.save(issue);

        return IssueResponse.fromEntity(savedIssue);
    }

    @Transactional
    public IssueResponse update(Long idIssue, IssueUpdateRequest request) {
        Issue issue = findIssueEntityById(idIssue);

        User assignee = null;

        if (request.idAssignee() != null) {
            assignee = userRepository.findById(request.idAssignee())
                    .orElseThrow(() -> new RuntimeException("No existe el usuario asignado con ID: " + request.idAssignee()));
        }

        issue.setTitle(request.title());
        issue.setDescription(request.description());
        issue.setType(request.type());
        issue.setStatus(request.status());
        issue.setPriority(request.priority());
        issue.setAssignee(assignee);
        issue.setUpdatedAt(LocalDateTime.now());

        Issue updatedIssue = issueRepository.save(issue);

        return IssueResponse.fromEntity(updatedIssue);
    }

    @Transactional
    public IssueResponse updateStatus(Long idIssue, String newStatus) {
        Issue issue = findIssueEntityById(idIssue);

        issue.setStatus(newStatus);
        issue.setUpdatedAt(LocalDateTime.now());

        Issue updatedIssue = issueRepository.save(issue);

        return IssueResponse.fromEntity(updatedIssue);
    }

    @Transactional
    public void delete(Long idIssue) {
        Issue issue = findIssueEntityById(idIssue);
        issueRepository.delete(issue);
    }

    private Issue findIssueEntityById(Long idIssue) {
        return issueRepository.findById(idIssue)
                .orElseThrow(() -> new RuntimeException("No existe el ticket con ID: " + idIssue));
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
}