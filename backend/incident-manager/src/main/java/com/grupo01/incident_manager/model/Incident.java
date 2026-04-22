package com.grupo01.incident_manager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Incident {
    private Long id;
    private String title;
    private String description;
    private String estado;

}
