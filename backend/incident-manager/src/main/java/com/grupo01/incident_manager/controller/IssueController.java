package com.grupo01.incident_manager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grupo01.incident_manager.dtos.issue.IssueCreateRequest;
import com.grupo01.incident_manager.dtos.issue.IssueResponse;
import com.grupo01.incident_manager.dtos.issue.IssueStatusUpdateRequest;
import com.grupo01.incident_manager.dtos.issue.IssueUpdateRequest;
import com.grupo01.incident_manager.service.IssueService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    @GetMapping
    public ResponseEntity<List<IssueResponse>> findAll() {
        return ResponseEntity.ok(issueService.findAll());
    }

    @GetMapping("/{idIssue}")
    public ResponseEntity<IssueResponse> findById(@PathVariable Long idIssue) {
        return ResponseEntity.ok(issueService.findById(idIssue));
    }

    @GetMapping("/code/{ticketCode}")
    public ResponseEntity<IssueResponse> findByTicketCode(@PathVariable String ticketCode) {
        return ResponseEntity.ok(issueService.findByTicketCode(ticketCode));
    }

    @GetMapping("/project/{idProject}")
    public ResponseEntity<List<IssueResponse>> findByProject(@PathVariable Long idProject) {
        return ResponseEntity.ok(issueService.findByProject(idProject));
    }

    @GetMapping("/project/{idProject}/status")
    public ResponseEntity<List<IssueResponse>> findByProjectAndStatus(
            @PathVariable Long idProject,
            @RequestParam String status
    ) {
        return ResponseEntity.ok(issueService.findByProjectAndStatus(idProject, status));
    }

    @GetMapping("/assigned/{idUser}")
    public ResponseEntity<List<IssueResponse>> findAssignedOpenIssues(@PathVariable Long idUser) {
        return ResponseEntity.ok(issueService.findAssignedOpenIssues(idUser));
    }

    @PostMapping
    public ResponseEntity<IssueResponse> create(@Valid @RequestBody IssueCreateRequest request) {
        IssueResponse createdIssue = issueService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdIssue);
    }

    @PutMapping("/{idIssue}")
    public ResponseEntity<IssueResponse> update(
            @PathVariable Long idIssue,
            @Valid @RequestBody IssueUpdateRequest request
    ) {
        return ResponseEntity.ok(issueService.update(idIssue, request));
    }

    @PatchMapping("/{idIssue}/status")
    public ResponseEntity<IssueResponse> updateStatus(
            @PathVariable Long idIssue,
            @Valid @RequestBody IssueStatusUpdateRequest request
    ) {
        return ResponseEntity.ok(issueService.updateStatus(idIssue, request.status()));
    }

    @DeleteMapping("/{idIssue}")
    public ResponseEntity<Void> delete(@PathVariable Long idIssue) {
        issueService.delete(idIssue);
        return ResponseEntity.noContent().build();
    }
}