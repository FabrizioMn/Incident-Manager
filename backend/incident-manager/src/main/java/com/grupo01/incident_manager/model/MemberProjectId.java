package com.grupo01.incident_manager.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberProjectId implements Serializable {

    @Column(name = "id_project")
    private Long idProject;

    @Column(name = "id_user")
    private Long idUser;
}
