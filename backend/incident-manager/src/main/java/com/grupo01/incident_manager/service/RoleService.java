package com.grupo01.incident_manager.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo01.incident_manager.dtos.role.RoleCreateRequest;
import com.grupo01.incident_manager.dtos.role.RoleResponse;
import com.grupo01.incident_manager.dtos.role.RoleUpdateRequest;
import com.grupo01.incident_manager.model.Role;
import com.grupo01.incident_manager.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<RoleResponse> findAll() {
        return roleRepository.findAll()
                .stream()
                .map(RoleResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public RoleResponse findById(Long idRole) {
        Role role = findRoleEntityById(idRole);
        return RoleResponse.fromEntity(role);
    }

    @Transactional(readOnly = true)
    public RoleResponse findByName(String name) {
        String normalizedName = normalizeRoleName(name);

        Role role = roleRepository.findAll()
                .stream()
                .filter(existingRole -> existingRole.getName().equalsIgnoreCase(normalizedName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No existe un rol con nombre: " + normalizedName));

        return RoleResponse.fromEntity(role);
    }

    @Transactional
    public RoleResponse create(RoleCreateRequest request) {
        String normalizedName = normalizeRoleName(request.name());

        boolean roleAlreadyExists = roleRepository.findAll()
                .stream()
                .anyMatch(existingRole -> existingRole.getName().equalsIgnoreCase(normalizedName));

        if (roleAlreadyExists) {
            throw new RuntimeException("Ya existe un rol con nombre: " + normalizedName);
        }

        Role role = new Role();
        role.setName(normalizedName);
        role.setDescription(request.description());

        Role savedRole = roleRepository.save(role);

        return RoleResponse.fromEntity(savedRole);
    }

    @Transactional
    public RoleResponse update(Long idRole, RoleUpdateRequest request) {
        Role role = findRoleEntityById(idRole);

        String normalizedName = normalizeRoleName(request.name());

        boolean nameBelongsToAnotherRole = roleRepository.findAll()
                .stream()
                .anyMatch(existingRole ->
                        existingRole.getName().equalsIgnoreCase(normalizedName)
                                && !existingRole.getId().equals(idRole)
                );

        if (nameBelongsToAnotherRole) {
            throw new RuntimeException("Ya existe otro rol con nombre: " + normalizedName);
        }

        role.setName(normalizedName);
        role.setDescription(request.description());

        Role updatedRole = roleRepository.save(role);

        return RoleResponse.fromEntity(updatedRole);
    }

    @Transactional
    public void delete(Long idRole) {
        Role role = findRoleEntityById(idRole);

        try {
            roleRepository.delete(role);
        } catch (DataIntegrityViolationException exception) {
            throw new RuntimeException(
                    "No se puede eliminar el rol porque está asignado a uno o más usuarios."
            );
        }
    }

    private Role findRoleEntityById(Long idRole) {
        return roleRepository.findById(idRole)
                .orElseThrow(() -> new RuntimeException("No existe el rol con ID: " + idRole));
    }

    private String normalizeRoleName(String name) {
        if (name == null || name.isBlank()) {
            throw new RuntimeException("El nombre del rol es obligatorio");
        }

        return name.trim().toUpperCase();
    }
}