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

import com.grupo01.incident_manager.dtos.issuescheme.IssueSchemeCreateRequest;
import com.grupo01.incident_manager.dtos.issuescheme.IssueSchemeResponse;
import com.grupo01.incident_manager.dtos.issuescheme.IssueSchemeUpdateRequest;
import com.grupo01.incident_manager.service.IssueSchemeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/issue-schemes")
@RequiredArgsConstructor
public class IssueSchemeController {

    private final IssueSchemeService issueSchemeService;

    @GetMapping
    public ResponseEntity<List<IssueSchemeResponse>> findAll() {
        return ResponseEntity.ok(issueSchemeService.findAll());
    }

    @GetMapping("/{idIssueScheme}")
    public ResponseEntity<IssueSchemeResponse> findById(@PathVariable Long idIssueScheme) {
        return ResponseEntity.ok(issueSchemeService.findById(idIssueScheme));
    }

    @PostMapping
    public ResponseEntity<IssueSchemeResponse> create(
            @Valid @RequestBody IssueSchemeCreateRequest request
    ) {
        IssueSchemeResponse createdIssueScheme = issueSchemeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdIssueScheme);
    }

    @PutMapping("/{idIssueScheme}")
    public ResponseEntity<IssueSchemeResponse> update(
            @PathVariable Long idIssueScheme,
            @Valid @RequestBody IssueSchemeUpdateRequest request
    ) {
        return ResponseEntity.ok(issueSchemeService.update(idIssueScheme, request));
    }

    @DeleteMapping("/{idIssueScheme}")
    public ResponseEntity<Void> delete(@PathVariable Long idIssueScheme) {
        issueSchemeService.delete(idIssueScheme);
        return ResponseEntity.noContent().build();
    }
}