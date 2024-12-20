package com.guarderia.GuarderiaControl.model;

import com.guarderia.GuarderiaControl.util.EstadoPago;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

// Pago.java
@Entity
@Data
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fechaCorte;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estado;

    @Column(nullable = false)
    private Double monto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "nino_id", nullable = false)
    private Nino nino;

    @Column(nullable = false)
    private Double montoPagado = 0.0; // Nuevo campo

    // Método auxiliar para saber el monto pendiente
    public double getMontoPendiente() {
        return monto - montoPagado;
    }
}

