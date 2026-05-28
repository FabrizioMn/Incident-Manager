package com.grupo01.incident_manager.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.grupo01.incident_manager.model.IssueScheme;
import com.grupo01.incident_manager.model.Project;
import com.grupo01.incident_manager.model.Role;
import com.grupo01.incident_manager.model.User;
import com.grupo01.incident_manager.repository.IssueSchemeRepository;
import com.grupo01.incident_manager.repository.ProjectRepository;
import com.grupo01.incident_manager.repository.RoleRepository;
import com.grupo01.incident_manager.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            RoleRepository roleRepository,
            UserRepository userRepository,
            IssueSchemeRepository issueSchemeRepository,
            ProjectRepository projectRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            Role adminRole = roleRepository.findAll()
                    .stream()
                    .filter(role -> role.getName().equalsIgnoreCase("ADMIN"))
                    .findFirst()
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("ADMIN");
                        role.setDescription("Administrador del sistema");
                        return roleRepository.save(role);
                    });

            Role userRole = roleRepository.findAll()
                    .stream()
                    .filter(role -> role.getName().equalsIgnoreCase("USER"))
                    .findFirst()
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("USER");
                        role.setDescription("Usuario general del sistema");
                        return roleRepository.save(role);
                    });

            User adminUser = userRepository.findByEmail("admin@gmail.com")
                    .orElseGet(() -> {
                        User user = new User();
                        user.setName("Administrador");
                        user.setEmail("admin@gmail.com");
                        user.setPassword(passwordEncoder.encode("123456"));
                        user.setRole(adminRole);
                        return userRepository.save(user);
                    });

            userRepository.findByEmail("user@gmail.com")
                    .orElseGet(() -> {
                        User user = new User();
                        user.setName("Usuario Prueba");
                        user.setEmail("user@gmail.com");
                        user.setPassword(passwordEncoder.encode("123456"));
                        user.setRole(userRole);
                        return userRepository.save(user);
                    });

            IssueScheme issueScheme = issueSchemeRepository.findAll()
                    .stream()
                    .filter(scheme -> scheme.getName().equalsIgnoreCase("Esquema general"))
                    .findFirst()
                    .orElseGet(() -> {
                        IssueScheme scheme = new IssueScheme();
                        scheme.setName("Esquema general");
                        scheme.setDescription("Esquema inicial para tickets tipo BUG, TAREA y MEJORA");
                        return issueSchemeRepository.save(scheme);
                    });

            projectRepository.findAll()
                    .stream()
                    .filter(project -> project.getKey().equalsIgnoreCase("INC"))
                    .findFirst()
                    .orElseGet(() -> {
                        Project project = new Project();
                        project.setName("Sistema de Incidentes");
                        project.setKey("INC");
                        project.setDescription("Proyecto inicial para pruebas del sistema de incidencias");
                        project.setAuthor(adminUser);
                        project.setScheme(issueScheme);
                        return projectRepository.save(project);
                    });

            System.out.println("Datos iniciales cargados correctamente.");
        };
    }
}