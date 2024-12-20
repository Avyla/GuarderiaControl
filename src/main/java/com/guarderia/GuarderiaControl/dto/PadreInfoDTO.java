package com.guarderia.GuarderiaControl.dto;

import com.guarderia.GuarderiaControl.model.Padre;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PadreInfoDTO {

    private Long id;
    private String nombreCompleto;
    private String telefono;
    private String email;
    private List<NinoSimpleInfoDTO> ninos;

    public PadreInfoDTO(Padre padre) {
        this.id = padre.getId();
        this.nombreCompleto = padre.getNombreCompleto();
        this.telefono = padre.getTelefono();
        this.email = padre.getEmail();
        // Manejo seguro de la lista
        this.ninos = padre.getNinos() != null
                ? padre.getNinos().stream()
                .map(NinoSimpleInfoDTO::new)
                .collect(Collectors.toList())
                : new ArrayList<>();
    }


}
