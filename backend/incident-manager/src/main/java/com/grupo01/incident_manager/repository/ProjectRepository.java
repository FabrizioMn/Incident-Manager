package com.grupo01.incident_manager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupo01.incident_manager.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Long, Project> {
    Optional<Project> findByKey(String key);

    boolean existsByKey(String key);
}
