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

import com.grupo01.incident_manager.dtos.user.UserCreateRequest;
import com.grupo01.incident_manager.dtos.user.UserPasswordUpdateRequest;
import com.grupo01.incident_manager.dtos.user.UserResponse;
import com.grupo01.incident_manager.dtos.user.UserRoleUpdateRequest;
import com.grupo01.incident_manager.dtos.user.UserUpdateRequest;
import com.grupo01.incident_manager.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{idUser}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long idUser) {
        return ResponseEntity.ok(userService.findById(idUser));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserCreateRequest request) {
        UserResponse createdUser = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{idUser}")
    public ResponseEntity<UserResponse> update(
            @PathVariable Long idUser,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        return ResponseEntity.ok(userService.update(idUser, request));
    }

    @PatchMapping("/{idUser}/role")
    public ResponseEntity<UserResponse> updateRole(
            @PathVariable Long idUser,
            @Valid @RequestBody UserRoleUpdateRequest request
    ) {
        return ResponseEntity.ok(userService.updateRole(idUser, request));
    }

    @PatchMapping("/{idUser}/password")
    public ResponseEntity<UserResponse> updatePassword(
            @PathVariable Long idUser,
            @Valid @RequestBody UserPasswordUpdateRequest request
    ) {
        return ResponseEntity.ok(userService.updatePassword(idUser, request));
    }

    @DeleteMapping("/{idUser}")
    public ResponseEntity<Void> delete(@PathVariable Long idUser) {
        userService.delete(idUser);
        return ResponseEntity.noContent().build();
    }
}