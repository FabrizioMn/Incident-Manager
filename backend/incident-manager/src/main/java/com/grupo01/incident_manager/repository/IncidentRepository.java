package com.grupo01.incident_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupo01.incident_manager.model.Incident;

@Repository
public interface IncidentRepository extends JpaRepository<Incident,Long> {
    
}
