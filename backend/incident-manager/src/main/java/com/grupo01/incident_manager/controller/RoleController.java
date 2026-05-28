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

import com.grupo01.incident_manager.dtos.role.RoleCreateRequest;
import com.grupo01.incident_manager.dtos.role.RoleResponse;
import com.grupo01.incident_manager.dtos.role.RoleUpdateRequest;
import com.grupo01.incident_manager.service.RoleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleResponse>> findAll() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("/{idRole}")
    public ResponseEntity<RoleResponse> findById(@PathVariable Long idRole) {
        return ResponseEntity.ok(roleService.findById(idRole));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<RoleResponse> findByName(@PathVariable String name) {
        return ResponseEntity.ok(roleService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<RoleResponse> create(@Valid @RequestBody RoleCreateRequest request) {
        RoleResponse createdRole = roleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    @PutMapping("/{idRole}")
    public ResponseEntity<RoleResponse> update(
            @PathVariable Long idRole,
            @Valid @RequestBody RoleUpdateRequest request
    ) {
        return ResponseEntity.ok(roleService.update(idRole, request));
    }

    @DeleteMapping("/{idRole}")
    public ResponseEntity<Void> delete(@PathVariable Long idRole) {
        roleService.delete(idRole);
        return ResponseEntity.noContent().build();
    }
}