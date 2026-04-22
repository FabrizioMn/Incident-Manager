package com.grupo01.incident_manager;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.grupo01.incident_manager.model.Incident;
import com.grupo01.incident_manager.service.IncidentService;

public class IncidentServiceTest {

    private IncidentService service = new IncidentService(null);

    @Test
    @DisplayName("Debe lanzar una excepcion si el titulo de la incidencia es null")
    public void shouldThrowExceptionWhenTitleIsNull() {
        Incident invalidIncident = new Incident();
        invalidIncident.setTitle(null);

        assertThrows(IllegalArgumentException.class, () -> service.createIncident(invalidIncident));
    }
}
