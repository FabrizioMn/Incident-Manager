package com.grupo01.incident_manager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.grupo01.incident_manager.model.Incident;
import com.grupo01.incident_manager.repository.IncidentRepository;

@Service
public class IncidentService {

    private final IncidentRepository repository;

    public IncidentService(IncidentRepository repository) {
        this.repository = repository;
    }

    public List<Incident> getAll() {
        return repository.findAll();
    }

    public Incident createIncident(Incident incident) {
        if (incident.getTitle() == null || incident.getTitle().isEmpty()) {
            throw new IllegalArgumentException("El titulo es obligatorio");
        }
        incident.setEstado("OPEN");
        return repository.save(incident);
    }

    public Incident updateIncident(Long id, Incident newincident) {
        return repository.findById(id)
                .map(incident -> {
                    incident.setTitle(newincident.getTitle());
                    incident.setDescription(newincident.getDescription());
                    incident.setEstado(newincident.getEstado());
                    return repository.save(incident);
                }).orElseThrow(() -> new RuntimeException("Incidencia no encontrada"));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: ID no encontrada");
        }
        repository.deleteById(id);
    }
}
