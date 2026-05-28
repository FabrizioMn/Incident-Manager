package com.grupo01.incident_manager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.grupo01.incident_manager.model.Issue;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

    // Listar tickets de un proyecto especifico agrupadas por su estado
    List<Issue> findByProject_IdAndStatus(Long idProject, String status);

    // Ver todos los tickets de un proyecto
    List<Issue> findByProject_Id(Long idProject);

    // Para ver que los tickets que tiene asignado un usuario en especifico
    List<Issue> findByAssignee_IdAndStatusNot(Long idUser, String status);

    // Para buscar un ticket por su codigo
    Optional<Issue> findByTicketCode(String ticketCode);

    @Query("SELECT COUNT(i) FROM Issue i WHERE i.project.id=:idProject")
    long countByProject_Id(@Param("idProject") Long idProject);
}
