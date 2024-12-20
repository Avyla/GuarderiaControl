package com.guarderia.GuarderiaControl.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre; // Ejemplo: "DOCENTE", "ADMINISTRADOR"
}

