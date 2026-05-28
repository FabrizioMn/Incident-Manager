package com.grupo01.incident_manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupo01.incident_manager.model.MemberProject;
import com.grupo01.incident_manager.model.MemberProjectId;

@Repository
public interface MemberProjectRepository extends JpaRepository<MemberProject, MemberProjectId> {

    // Para que un usuario pueda ver en que proyectos esta activo
    List<MemberProject> findByUser_Id(Long idUser);

    // Para ver el equipo de trabajo de un proyecto
    List<MemberProject> findByProject_Id(Long idProject);

    // Para saber si un usuario ya existe en un proyecto
    boolean existsByProject_IdAndUser_Id(Long idProject, Long idUser);

}
