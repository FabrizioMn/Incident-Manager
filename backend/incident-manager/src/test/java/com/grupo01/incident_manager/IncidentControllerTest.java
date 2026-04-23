package com.grupo01.incident_manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.grupo01.incident_manager.model.Incident;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IncidentControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Debe crear una incidencia")
    public void createIncident() {
        Incident newIncident = new Incident();
        newIncident.setTitle("Fallo de servidor");
        newIncident.setDescription("El servidor no responde");

        ResponseEntity<Incident> response = restTemplate.postForEntity(
                "/api/incidents",
                newIncident,
                Incident.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Fallo de servidor", response.getBody().getTitle());
    }

    @Test
    @DisplayName("Debe listar las incidencias guardadas")
    public void listIncidents() {
        ResponseEntity<Incident[]> response = restTemplate.getForEntity(
                "/api/incidents",
                Incident[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Debe actualizar una incidencia")
    public void updateIncident() {
        Incident original = new Incident();
        original.setTitle("Error Original");

        ResponseEntity<Incident> postResponse = restTemplate.postForEntity("/api/incidents", original, Incident.class);
        Long id = postResponse.getBody().getId();

        Incident changes = new Incident();
        changes.setTitle("Error Actualizado");
        changes.setDescription("Nueva descripción");

        HttpEntity<Incident> requestEntity = new HttpEntity<>(changes);
        ResponseEntity<Incident> putResponse = restTemplate.exchange(
                "/api/incidents/" + id,
                HttpMethod.PUT,
                requestEntity,
                Incident.class);

        assertEquals(HttpStatus.OK, putResponse.getStatusCode());
        assertEquals("Error Actualizado", putResponse.getBody().getTitle());
    }

    @Test
    @DisplayName("Debe eliminar una incidencia")
    public void deleteIncident() {
        Incident newIncident = new Incident();
        newIncident.setTitle("Para borrar");

        ResponseEntity<Incident> postResponse = restTemplate.postForEntity("/api/incidents", newIncident,
                Incident.class);
        Long id = postResponse.getBody().getId();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/incidents/" + id,
                HttpMethod.DELETE,
                null,
                Void.class);

        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
    }
}
