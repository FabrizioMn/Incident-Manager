package com.grupo01.incident_manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.grupo01.incident_manager.model.Incident;
import com.grupo01.incident_manager.repository.IncidentRepository;
import com.grupo01.incident_manager.service.IncidentService;

@SpringBootTest
public class IncidentServiceTest {

    @Autowired
    private IncidentService service;

    @Autowired
    private IncidentRepository repository;

    @Test
    @DisplayName("Debe lanzar una excepcion si el titulo de la incidencia es null")
    public void shouldThrowExceptionWhenTitleIsNull() {
        Incident invalidIncident = new Incident();
        invalidIncident.setTitle(null);

        assertThrows(IllegalArgumentException.class, () -> service.createIncident(invalidIncident));
    }

    @Test
    @DisplayName("Debe crear una incidencia correctamente")
    public void createIncident() {
        Incident newIncident = new Incident();
        newIncident.setTitle("primera incidencia");
        newIncident.setDescription("texto descriptivo");

        Incident saveIncident = service.createIncident(newIncident);

        assertNotNull(saveIncident.getId());
        assertEquals("primera incidencia", saveIncident.getTitle());
        assertEquals("texto descriptivo", saveIncident.getDescription());
        assertEquals("OPEN", saveIncident.getEstado());
    }

    @Test
    @DisplayName("Debe actualizar una incidencia correctamente")
    public void updateIncident() {
        Incident incident = new Incident();
        incident.setTitle("Original");
        Incident savedIncident = service.createIncident(incident);

        Incident update = new Incident();
        update.setTitle("Actualizado");

        Incident updated = service.updateIncident(savedIncident.getId(), update);

        assertEquals("Actualizado", updated.getTitle());
    }

    @Test
    @DisplayName("Debe eliminar una incidencia")
    public void deleteIncident() {
        Incident incident = new Incident();
        incident.setTitle("Para borrar");
        Incident saved = service.createIncident(incident);

        service.delete(saved.getId());

        assertFalse(repository.existsById(saved.getId()));
    }

}
