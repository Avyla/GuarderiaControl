package com.guarderia.GuarderiaControl.dto;

import com.guarderia.GuarderiaControl.model.Pago;
import com.guarderia.GuarderiaControl.util.EstadoPago;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PagoInfoDTO {

    private Long id;
    private LocalDate fechaCorte;
    private EstadoPago estado;
    private Double monto;

    public PagoInfoDTO(Pago pago){
        this.id = pago.getId();
        this.fechaCorte = pago.getFechaCorte();
        this.estado = pago.getEstado();
        this.monto = pago.getMonto();
    }

}
