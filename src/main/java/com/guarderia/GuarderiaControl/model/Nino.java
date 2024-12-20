package com.guarderia.GuarderiaControl.model;

import com.guarderia.GuarderiaControl.util.TipoCuidado;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Nino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreCompleto;

    @Column(nullable = false)
    private String fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCuidado tipoCuidado;

    @OneToMany(mappedBy = "nino", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pago> pagos = new ArrayList<>();

    @ManyToOne(optional = false) // Relacion obligatoria con el padre
    @JoinColumn(name = "padre_id", nullable = false)
    private Padre padre;
}

