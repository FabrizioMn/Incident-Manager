package com.grupo01.incident_manager.service;

import org.springframework.stereotype.Service;

import com.grupo01.incident_manager.model.Incident;
import com.grupo01.incident_manager.repository.IncidentRepository;

@Service
public class IncidentService {

    private final IncidentRepository repository;

    public IncidentService(IncidentRepository repository) {
        this.repository = repository;
    }

    public Incident createIncident(Incident incident) {
        if (incident.getTitle() == null) {
            throw new IllegalArgumentException("El titulo no puede ser nulo");
        }
        return repository.save(incident);
    }
}
