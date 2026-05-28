package com.grupo01.incident_manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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

import com.grupo01.incident_manager.dtos.issue.IssueRequest;
import com.grupo01.incident_manager.dtos.issue.IssueResponse;
import com.grupo01.incident_manager.model.Issue;
import com.grupo01.incident_manager.model.Project;
import com.grupo01.incident_manager.model.User;
import com.grupo01.incident_manager.repository.IssueRepository;
import com.grupo01.incident_manager.repository.MemberProjectRepository;
import com.grupo01.incident_manager.repository.ProjectRepository;
import com.grupo01.incident_manager.repository.UserRepository;
import com.grupo01.incident_manager.service.IssueService;

@ExtendWith(MockitoExtension.class)
public class IssueServiceTest {

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MemberProjectRepository memberProjectRepository;

    @InjectMocks
    private IssueService issueService;

    private Project sampleProject;
    private User sampleCreator;
    private User sampleAssignee;

    @BeforeEach
    void setUp() {
        // Inicializamos objetos de prueba base antes de cada test
        sampleProject = new Project();
        sampleProject.setId(1L);
        sampleProject.setKey("API");
        sampleProject.setName("Jira Project");

        sampleCreator = new User();
        sampleCreator.setId(1L);
        sampleCreator.setName("Fabrizio Medina");
        sampleCreator.setEmail("fabrizio@example.com");

        sampleAssignee = new User();
        sampleAssignee.setId(2L);
        sampleAssignee.setName("Dev User");
        sampleAssignee.setEmail("dev@example.com");
    }

    @Test
    @DisplayName("Debería crear una incidencia exitosamente con código correlativo autogenerado")
    void createIssueSuccess() {
        IssueRequest request = new IssueRequest(
                "Configurar JWT", "Descripción de la tarea",
                "TASK", "HIGH", 1L, 1L, 2L);

        Issue savedIssue = Issue.builder()
                .id(100L)
                .ticketCode("API-1")
                .title("Configurar JWT")
                .description("Descripción de la tarea")
                .type("TASK")
                .status("BACKLOG")
                .priority("HIGH")
                .project(sampleProject)
                .creator(sampleCreator)
                .assignee(sampleAssignee)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Configuramos las respuestas de los mocks
        when(projectRepository.findById(1L)).thenReturn(Optional.of(sampleProject));
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleCreator));
        when(userRepository.findById(2L)).thenReturn(Optional.of(sampleAssignee));

        // Simulamos que el usuario asignado SÍ pertenece al proyecto
        when(memberProjectRepository.existsByProject_IdAndUser_Id(1L, 2L)).thenReturn(true);
        // Simulamos que actualmente hay 0 tickets en el proyecto
        when(issueRepository.countByProject_Id(1L)).thenReturn(0L);
        // Simulamos el guardado
        when(issueRepository.save(any(Issue.class))).thenReturn(savedIssue);

        // WHEN (Ejecución del método real)
        IssueResponse response = issueService.createIssue(request);

        // THEN (Verificaciones de que la lógica de negocio hizo lo correcto)
        assertNotNull(response);
        assertEquals("API-1", response.tickerCode());
        assertEquals("BACKLOG", response.status());
        assertEquals("Fabrizio Medina", response.creatorName());
        assertEquals("Dev User", response.assigneeName());

        // Verificamos que se llamaron a los repositorios las veces necesarias
        verify(issueRepository, times(1)).save(any(Issue.class));
        verify(issueRepository, times(1)).countByProject_Id(1L);
    }

}
