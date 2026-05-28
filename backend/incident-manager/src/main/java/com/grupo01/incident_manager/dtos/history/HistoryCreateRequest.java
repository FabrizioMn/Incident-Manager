package com.grupo01.incident_manager.dtos.history;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record HistoryCreateRequest(

        @NotNull(message = "El issue es obligatorio")
        Long idIssue,

        @NotNull(message = "El usuario es obligatorio")
        Long idUser,

        @NotBlank(message = "El campo cambiado es obligatorio")
        @Size(max = 100, message = "El campo cambiado no puede superar los 100 caracteres")
        String changedField,

        String oldValue,

        String newValue
) {
}