package com.grupo01.incident_manager.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo01.incident_manager.dtos.issuescheme.IssueSchemeCreateRequest;
import com.grupo01.incident_manager.dtos.issuescheme.IssueSchemeResponse;
import com.grupo01.incident_manager.dtos.issuescheme.IssueSchemeUpdateRequest;
import com.grupo01.incident_manager.model.IssueScheme;
import com.grupo01.incident_manager.repository.IssueSchemeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IssueSchemeService {

    private final IssueSchemeRepository issueSchemeRepository;

    @Transactional(readOnly = true)
    public List<IssueSchemeResponse> findAll() {
        return issueSchemeRepository.findAll()
                .stream()
                .map(IssueSchemeResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public IssueSchemeResponse findById(Long idIssueScheme) {
        IssueScheme issueScheme = findIssueSchemeEntityById(idIssueScheme);
        return IssueSchemeResponse.fromEntity(issueScheme);
    }

    @Transactional
    public IssueSchemeResponse create(IssueSchemeCreateRequest request) {
        IssueScheme issueScheme = new IssueScheme();
        issueScheme.setName(request.name());
        issueScheme.setDescription(request.description());

        IssueScheme savedIssueScheme = issueSchemeRepository.save(issueScheme);

        return IssueSchemeResponse.fromEntity(savedIssueScheme);
    }

    @Transactional
    public IssueSchemeResponse update(Long idIssueScheme, IssueSchemeUpdateRequest request) {
        IssueScheme issueScheme = findIssueSchemeEntityById(idIssueScheme);

        issueScheme.setName(request.name());
        issueScheme.setDescription(request.description());

        IssueScheme updatedIssueScheme = issueSchemeRepository.save(issueScheme);

        return IssueSchemeResponse.fromEntity(updatedIssueScheme);
    }

    @Transactional
    public void delete(Long idIssueScheme) {
        IssueScheme issueScheme = findIssueSchemeEntityById(idIssueScheme);

        try {
            issueSchemeRepository.delete(issueScheme);
        } catch (DataIntegrityViolationException exception) {
            throw new RuntimeException(
                    "No se puede eliminar el esquema porque está siendo usado por uno o más proyectos."
            );
        }
    }

    private IssueScheme findIssueSchemeEntityById(Long idIssueScheme) {
        return issueSchemeRepository.findById(idIssueScheme)
                .orElseThrow(() -> new RuntimeException(
                        "No existe el esquema de issues con ID: " + idIssueScheme
                ));
    }
}