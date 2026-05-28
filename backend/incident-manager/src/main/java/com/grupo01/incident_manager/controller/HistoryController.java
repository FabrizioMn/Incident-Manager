package com.grupo01.incident_manager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupo01.incident_manager.dtos.history.HistoryCreateRequest;
import com.grupo01.incident_manager.dtos.history.HistoryResponse;
import com.grupo01.incident_manager.service.HistoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/histories")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping
    public ResponseEntity<List<HistoryResponse>> findAll() {
        return ResponseEntity.ok(historyService.findAll());
    }

    @GetMapping("/{idHistory}")
    public ResponseEntity<HistoryResponse> findById(@PathVariable Long idHistory) {
        return ResponseEntity.ok(historyService.findById(idHistory));
    }

    @GetMapping("/issue/{idIssue}")
    public ResponseEntity<List<HistoryResponse>> findByIssue(@PathVariable Long idIssue) {
        return ResponseEntity.ok(historyService.findByIssue(idIssue));
    }

    @PostMapping
    public ResponseEntity<HistoryResponse> create(
            @Valid @RequestBody HistoryCreateRequest request
    ) {
        HistoryResponse createdHistory = historyService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHistory);
    }

    @DeleteMapping("/{idHistory}")
    public ResponseEntity<Void> delete(@PathVariable Long idHistory) {
        historyService.delete(idHistory);
        return ResponseEntity.noContent().build();
    }
}