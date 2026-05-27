package com.grupo01.incident_manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupo01.incident_manager.model.MemberProject;

@Repository
public interface MemberProjectRepository extends JpaRepository<Long, MemberProject> {

    // Para que un usuario pueda ver en que proyectos esta activo
    List<MemberProject> findById_IdUser(Long idUser);

    // Para ver el equipo de trabajo de un proyecto
    List<MemberProject> findById_IdProject(Long idProject);

}
