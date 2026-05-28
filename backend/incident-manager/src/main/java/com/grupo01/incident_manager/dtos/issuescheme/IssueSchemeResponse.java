package com.grupo01.incident_manager.dtos.issuescheme;

import com.grupo01.incident_manager.model.IssueScheme;

public record IssueSchemeResponse(
        Long id,
        String name,
        String description
) {

    public static IssueSchemeResponse fromEntity(IssueScheme issueScheme) {
        return new IssueSchemeResponse(
                issueScheme.getId(),
                issueScheme.getName(),
                issueScheme.getDescription()
        );
    }
}