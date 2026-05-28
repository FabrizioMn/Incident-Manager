package com.grupo01.incident_manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grupo01.incident_manager.dtos.project.ProjectRequest;
import com.grupo01.incident_manager.dtos.project.ProjectResponse;
import com.grupo01.incident_manager.model.IssueScheme;
import com.grupo01.incident_manager.model.Project;
import com.grupo01.incident_manager.model.User;
import com.grupo01.incident_manager.repository.IssueSchemeRepository;
import com.grupo01.incident_manager.repository.ProjectRepository;
import com.grupo01.incident_manager.repository.UserRepository;
import com.grupo01.incident_manager.service.ProjectService;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private IssueSchemeRepository issueSchemeRepository;

    @InjectMocks
    private ProjectService projectService;

    private User sampleUser;
    private IssueScheme sampleScheme;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setName("Fabrizio Medina");

        sampleScheme = new IssueScheme();
        sampleScheme.setId(1L);
        sampleScheme.setName("Scrum Default Scheme");
    }

    @Test
    @DisplayName("Debería crear un proyecto exitosamente forzando la KEY a mayúsculas")
    void createProjectSuccess() {
        ProjectRequest request = new ProjectRequest(
                "Jira Clone", " api  ", "Descripción del proyecto", 1L, 1L);

        Project savedProject = Project.builder()
                .id(10L)
                .name("Jira Clone")
                .key("API")
                .description("Descripción del proyecto")
                .author(sampleUser)
                .scheme(sampleScheme)
                .createdAt(LocalDateTime.now())
                .build();

        // Comportamientos simulados
        when(projectRepository.existsByKey("API")).thenReturn(false); // No existe duplicado
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(issueSchemeRepository.findById(1L)).thenReturn(Optional.of(sampleScheme));
        when(projectRepository.save(any(Project.class))).thenReturn(savedProject);

        // WHEN: Ejecutamos la lógica de negocio real
        ProjectResponse response = projectService.createProject(request);

        // THEN: Verificamos los resultados
        assertNotNull(response);
        assertEquals(10L, response.id());
        assertEquals("API", response.key());
        assertEquals("Jira Clone", response.name());
        assertEquals("Scrum Default Scheme", response.issueSchemeName());
        assertEquals("Fabrizio Medina", response.userName());

        // Verificar que se interactuó con el repositorio de forma correcta
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    @DisplayName("Debería lanzar una excepción si la KEY del proyecto ya está registrada")
    void createProjectThrowsExceptionWhenKeyExists() {
        // Intentamos registrar un proyecto con una KEY "API"
        ProjectRequest request = new ProjectRequest(
                "Otro Proyecto", "API", "Descripción", 1L, 1L);

        // Simulamos que la base de datos ya tiene ocupada esa KEY
        when(projectRepository.existsByKey("API")).thenReturn(true);

        // Evaluamos que explote con el mensaje controlado
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            projectService.createProject(request);
        });

        assertEquals("Ya existe un proyecto con la KEY: API", exception.getMessage());

        // Verificación de seguridad: Jamás debió avanzar a buscar usuarios ni a guardar
        // nada
        verify(userRepository, never()).findById(anyLong());
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    @DisplayName("Debería lanzar una excepción si el esquema de incidencias no existe")
    void createProjectThrowsExceptionWhenSchemeNotFound() {
        ProjectRequest request = new ProjectRequest(
                "Jira Clone", "API", "Descripción", 999L, 1L);

        when(projectRepository.existsByKey("API")).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        // Simulamos que el repositorio de esquemas devuelve un Optional vacío
        when(issueSchemeRepository.findById(999L)).thenReturn(Optional.empty());

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            projectService.createProject(request);
        });

        assertEquals("Esquema de incidencias no encontrado", exception.getMessage());
        verify(projectRepository, never()).save(any(Project.class));

    }
}
