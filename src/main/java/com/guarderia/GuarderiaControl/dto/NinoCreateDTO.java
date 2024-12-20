package com.guarderia.GuarderiaControl.dto;

import com.guarderia.GuarderiaControl.util.TipoCuidado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NinoCreateDTO {

    private Long id;

    @NotBlank(message = "El nombre completo es obligatorio")
    private String nombreCompleto;

    @NotBlank(message = "La fecha de nacimiento es obligatoria")
    private String fechaNacimiento;

    @NotNull(message = "El tipo de cuidado es obligatorio")
    private TipoCuidado tipoCuidado;

    // Quitar la anotación @NotNull aquí, validaremos manualmente en el controlador web
    private Long padreId;
}
