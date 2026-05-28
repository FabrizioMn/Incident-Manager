package com.grupo01.incident_manager.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo01.incident_manager.dtos.history.HistoryCreateRequest;
import com.grupo01.incident_manager.dtos.history.HistoryResponse;
import com.grupo01.incident_manager.model.History;
import com.grupo01.incident_manager.model.Issue;
import com.grupo01.incident_manager.model.User;
import com.grupo01.incident_manager.repository.HistoryRepository;
import com.grupo01.incident_manager.repository.IssueRepository;
import com.grupo01.incident_manager.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<HistoryResponse> findAll() {
        return historyRepository.findAll()
                .stream()
                .map(HistoryResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public HistoryResponse findById(Long idHistory) {
        History history = findHistoryEntityById(idHistory);
        return HistoryResponse.fromEntity(history);
    }

    @Transactional(readOnly = true)
    public List<HistoryResponse> findByIssue(Long idIssue) {
        validateIssueExists(idIssue);

        return historyRepository.findByIssue_IdOrderByChangedAtDesc(idIssue)
                .stream()
                .map(HistoryResponse::fromEntity)
                .toList();
    }

    @Transactional
    public HistoryResponse create(HistoryCreateRequest request) {
        Issue issue = issueRepository.findById(request.idIssue())
                .orElseThrow(() -> new RuntimeException("No existe el issue con ID: " + request.idIssue()));

        User user = userRepository.findById(request.idUser())
                .orElseThrow(() -> new RuntimeException("No existe el usuario con ID: " + request.idUser()));

        History history = new History();
        history.setIssue(issue);
        history.setUser(user);
        history.setChangedField(normalizeChangedField(request.changedField()));
        history.setOldValue(request.oldValue());
        history.setNewValue(request.newValue());

        History savedHistory = historyRepository.save(history);

        return HistoryResponse.fromEntity(savedHistory);
    }

    @Transactional
    public HistoryResponse registerChange(
            Long idIssue,
            Long idUser,
            String changedField,
            String oldValue,
            String newValue
    ) {
        Issue issue = issueRepository.findById(idIssue)
                .orElseThrow(() -> new RuntimeException("No existe el issue con ID: " + idIssue));

        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("No existe el usuario con ID: " + idUser));

        History history = new History();
        history.setIssue(issue);
        history.setUser(user);
        history.setChangedField(normalizeChangedField(changedField));
        history.setOldValue(oldValue);
        history.setNewValue(newValue);

        History savedHistory = historyRepository.save(history);

        return HistoryResponse.fromEntity(savedHistory);
    }

    @Transactional
    public void delete(Long idHistory) {
        History history = findHistoryEntityById(idHistory);
        historyRepository.delete(history);
    }

    private History findHistoryEntityById(Long idHistory) {
        return historyRepository.findById(idHistory)
                .orElseThrow(() -> new RuntimeException("No existe el historial con ID: " + idHistory));
    }

    private void validateIssueExists(Long idIssue) {
        if (!issueRepository.existsById(idIssue)) {
            throw new RuntimeException("No existe el issue con ID: " + idIssue);
        }
    }

    private String normalizeChangedField(String changedField) {
        if (changedField == null || changedField.isBlank()) {
            throw new RuntimeException("El campo cambiado es obligatorio");
        }

        return changedField.trim().toLowerCase();
    }
}