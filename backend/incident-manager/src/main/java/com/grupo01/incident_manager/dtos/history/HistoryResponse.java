package com.grupo01.incident_manager.dtos.history;

import java.time.LocalDateTime;

import com.grupo01.incident_manager.model.History;

public record HistoryResponse(
        Long id,
        String changedField,
        String oldValue,
        String newValue,
        LocalDateTime changedAt,
        Long idIssue,
        String ticketCode,
        Long idUser,
        String userName,
        String userEmail
) {

    public static HistoryResponse fromEntity(History history) {
        return new HistoryResponse(
                history.getId(),
                history.getChangedField(),
                history.getOldValue(),
                history.getNewValue(),
                history.getChangedAt(),
                history.getIssue() != null ? history.getIssue().getId() : null,
                history.getIssue() != null ? history.getIssue().getTicketCode() : null,
                history.getUser() != null ? history.getUser().getId() : null,
                history.getUser() != null ? history.getUser().getName() : null,
                history.getUser() != null ? history.getUser().getEmail() : null
        );
    }
}