package com.grupo01.incident_manager.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo01.incident_manager.dtos.user.UserCreateRequest;
import com.grupo01.incident_manager.dtos.user.UserPasswordUpdateRequest;
import com.grupo01.incident_manager.dtos.user.UserResponse;
import com.grupo01.incident_manager.dtos.user.UserRoleUpdateRequest;
import com.grupo01.incident_manager.dtos.user.UserUpdateRequest;
import com.grupo01.incident_manager.model.Role;
import com.grupo01.incident_manager.model.User;
import com.grupo01.incident_manager.repository.RoleRepository;
import com.grupo01.incident_manager.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long idUser) {
        User user = findUserEntityById(idUser);
        return UserResponse.fromEntity(user);
    }

    @Transactional(readOnly = true)
    public UserResponse findByEmail(String email) {
        String normalizedEmail = normalizeEmail(email);

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new RuntimeException("No existe un usuario con correo: " + normalizedEmail));

        return UserResponse.fromEntity(user);
    }

    @Transactional
    public UserResponse create(UserCreateRequest request) {
        String normalizedEmail = normalizeEmail(request.email());

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new RuntimeException("Ya existe un usuario con correo: " + normalizedEmail);
        }

        Role role = roleRepository.findById(request.idRole())
                .orElseThrow(() -> new RuntimeException("No existe el rol con ID: " + request.idRole()));

        User user = new User();
        user.setName(request.name());
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(role);

        User savedUser = userRepository.save(user);

        return UserResponse.fromEntity(savedUser);
    }

    @Transactional
    public UserResponse update(Long idUser, UserUpdateRequest request) {
        User user = findUserEntityById(idUser);

        String normalizedEmail = normalizeEmail(request.email());

        boolean emailBelongsToAnotherUser = userRepository.findByEmail(normalizedEmail)
                .filter(existingUser -> !existingUser.getId().equals(idUser))
                .isPresent();

        if (emailBelongsToAnotherUser) {
            throw new RuntimeException("Ya existe otro usuario con correo: " + normalizedEmail);
        }

        user.setName(request.name());
        user.setEmail(normalizedEmail);

        User updatedUser = userRepository.save(user);

        return UserResponse.fromEntity(updatedUser);
    }

    @Transactional
    public UserResponse updateRole(Long idUser, UserRoleUpdateRequest request) {
        User user = findUserEntityById(idUser);

        Role role = roleRepository.findById(request.idRole())
                .orElseThrow(() -> new RuntimeException("No existe el rol con ID: " + request.idRole()));

        user.setRole(role);

        User updatedUser = userRepository.save(user);

        return UserResponse.fromEntity(updatedUser);
    }

    @Transactional
    public UserResponse updatePassword(Long idUser, UserPasswordUpdateRequest request) {
        User user = findUserEntityById(idUser);

        user.setPassword(passwordEncoder.encode(request.newPassword()));

        User updatedUser = userRepository.save(user);

        return UserResponse.fromEntity(updatedUser);
    }

    @Transactional
    public void delete(Long idUser) {
        User user = findUserEntityById(idUser);

        try {
            userRepository.delete(user);
        } catch (DataIntegrityViolationException exception) {
            throw new RuntimeException(
                    "No se puede eliminar el usuario porque tiene información relacionada, como proyectos, issues, historial o tokens."
            );
        }
    }

    private User findUserEntityById(Long idUser) {
        return userRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("No existe el usuario con ID: " + idUser));
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new RuntimeException("El correo es obligatorio");
        }

        return email.trim().toLowerCase();
    }
}