package com.grupo01.incident_manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupo01.incident_manager.model.History;

@Repository
public interface HistoryRepository extends JpaRepository<Long, History> {

    // Para ver los cambios de un ticker en especifico, ordenado por fecha
    List<History> findByIssue_IdIssueOrderByChangedAtDesc(Long idIssue);
}
