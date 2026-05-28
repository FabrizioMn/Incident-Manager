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
import org.springframework.web.bind.annotation.RestController;

import com.grupo01.incident_manager.dtos.memberproject.MemberProjectCreateRequest;
import com.grupo01.incident_manager.dtos.memberproject.MemberProjectResponse;
import com.grupo01.incident_manager.dtos.memberproject.MemberProjectUpdateRequest;
import com.grupo01.incident_manager.service.MemberProjectService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/member-projects")
@RequiredArgsConstructor
public class MemberProjectController {

    private final MemberProjectService memberProjectService;

    @GetMapping
    public ResponseEntity<List<MemberProjectResponse>> findAll() {
        return ResponseEntity.ok(memberProjectService.findAll());
    }

    @GetMapping("/project/{idProject}")
    public ResponseEntity<List<MemberProjectResponse>> findMembersByProject(
            @PathVariable Long idProject
    ) {
        return ResponseEntity.ok(memberProjectService.findMembersByProject(idProject));
    }

    @GetMapping("/user/{idUser}")
    public ResponseEntity<List<MemberProjectResponse>> findProjectsByUser(
            @PathVariable Long idUser
    ) {
        return ResponseEntity.ok(memberProjectService.findProjectsByUser(idUser));
    }

    @GetMapping("/project/{idProject}/user/{idUser}")
    public ResponseEntity<MemberProjectResponse> findById(
            @PathVariable Long idProject,
            @PathVariable Long idUser
    ) {
        return ResponseEntity.ok(memberProjectService.findById(idProject, idUser));
    }

    @PostMapping
    public ResponseEntity<MemberProjectResponse> addMember(
            @Valid @RequestBody MemberProjectCreateRequest request
    ) {
        MemberProjectResponse createdMember = memberProjectService.addMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMember);
    }

    @PutMapping("/project/{idProject}/user/{idUser}")
    public ResponseEntity<MemberProjectResponse> updateMemberRole(
            @PathVariable Long idProject,
            @PathVariable Long idUser,
            @Valid @RequestBody MemberProjectUpdateRequest request
    ) {
        return ResponseEntity.ok(memberProjectService.updateMemberRole(idProject, idUser, request));
    }

    @PatchMapping("/project/{idProject}/user/{idUser}/role")
    public ResponseEntity<MemberProjectResponse> patchMemberRole(
            @PathVariable Long idProject,
            @PathVariable Long idUser,
            @Valid @RequestBody MemberProjectUpdateRequest request
    ) {
        return ResponseEntity.ok(memberProjectService.updateMemberRole(idProject, idUser, request));
    }

    @DeleteMapping("/project/{idProject}/user/{idUser}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long idProject,
            @PathVariable Long idUser
    ) {
        memberProjectService.removeMember(idProject, idUser);
        return ResponseEntity.noContent().build();
    }
}