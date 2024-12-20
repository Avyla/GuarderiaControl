package com.guarderia.GuarderiaControl.dto;

import com.guarderia.GuarderiaControl.model.Nino;
import com.guarderia.GuarderiaControl.util.TipoCuidado;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class NinoInfoDTO {
    private Long id;
    private String nombreCompleto;
    private String fechaNacimiento;
    private TipoCuidado tipoCuidado;
    private PadreSimpleInfoDTO padre;
    private List<PagoInfoDTO> pago;

    public NinoInfoDTO(Nino nino) {
        this.id = nino.getId();
        this.nombreCompleto = nino.getNombreCompleto();
        this.fechaNacimiento = nino.getFechaNacimiento();
        this.tipoCuidado = nino.getTipoCuidado();
        this.padre = new PadreSimpleInfoDTO(nino.getPadre());
        this.pago = nino.getPagos() != null
                ? nino.getPagos().stream()
                .map(PagoInfoDTO::new)
                .collect(Collectors.toList())
                : new ArrayList<>();
    }

}
