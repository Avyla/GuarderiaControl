package com.guarderia.GuarderiaControl.dto;

import com.guarderia.GuarderiaControl.model.Nino;
import com.guarderia.GuarderiaControl.util.TipoCuidado;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class NinoSimpleInfoDTO {
    private Long id;
    private String nombreCompleto;
    private String fechaNacimiento;
    private TipoCuidado tipoCuidado;
    private List<PagoInfoDTO> pago;

    public NinoSimpleInfoDTO(Nino nino) {
        this.id = nino.getId();
        this.nombreCompleto = nino.getNombreCompleto();
        this.fechaNacimiento = nino.getFechaNacimiento();
        this.tipoCuidado = nino.getTipoCuidado();
        this.pago = nino.getPagos() != null
                ? nino.getPagos().stream()
                .map(PagoInfoDTO::new)
                .collect(Collectors.toList())
                : new ArrayList<>();
    }
}
