package com.guarderia.GuarderiaControl.repository;

import com.guarderia.GuarderiaControl.model.Padre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PadreRepository extends JpaRepository<Padre, Long> {
    Optional<Padre> findByTelefono(String telefono);
}

