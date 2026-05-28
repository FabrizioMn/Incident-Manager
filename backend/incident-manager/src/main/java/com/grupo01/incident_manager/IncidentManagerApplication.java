package com.grupo01.incident_manager;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.grupo01.incident_manager.model.Role;
import com.grupo01.incident_manager.repository.RoleRepository;

@SpringBootApplication
public class IncidentManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(IncidentManagerApplication.class, args);
	}

	@Bean
	CommandLineRunner initRoles(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.count() == 0) {
				Role admin = new Role(null, "ADMIN", "Administrador");
				Role user = new Role(null, "USER", "Rol Usuario");
				roleRepository.saveAll(List.of(admin, user));
				System.out.println("Roles iniciales insertados");
			}
		};
	}

}
