package com.grupo01.incident_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupo01.incident_manager.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Long, Role> {

}
