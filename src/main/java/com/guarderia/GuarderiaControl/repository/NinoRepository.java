package com.guarderia.GuarderiaControl.repository;

import com.guarderia.GuarderiaControl.model.Nino;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NinoRepository extends JpaRepository<Nino, Long> {

    Optional<Nino> findByNombreCompletoAndFechaNacimiento(String nombreCompleto, String fechaNacimiento);
}

