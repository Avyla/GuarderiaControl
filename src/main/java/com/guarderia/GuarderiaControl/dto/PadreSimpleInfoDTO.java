package com.guarderia.GuarderiaControl.dto;

import com.guarderia.GuarderiaControl.model.Padre;
import lombok.Data;

@Data
public class PadreSimpleInfoDTO {
    private Long id;
    private String nombreCompleto;
    private String telefono;
    private String email;

    public PadreSimpleInfoDTO(Padre padre) {
        this.id = padre.getId();
        this.nombreCompleto = padre.getNombreCompleto();
        this.telefono = padre.getTelefono();
        this.email = padre.getEmail();
    }
}
