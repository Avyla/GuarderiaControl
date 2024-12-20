package com.guarderia.GuarderiaControl.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PadreCreateDTO {
    private Long id;

    @NotBlank(message = "El nombre completo es obligatorio")
    private String nombreCompleto;

    @NotBlank(message = "El telefono es obligatorio")
    private String telefono;

    private String email;
}

