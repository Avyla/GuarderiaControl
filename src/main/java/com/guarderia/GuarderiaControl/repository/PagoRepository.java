package com.guarderia.GuarderiaControl.repository;

import com.guarderia.GuarderiaControl.model.Pago;
import com.guarderia.GuarderiaControl.util.EstadoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    int countByNinoIdAndEstado(Long ninoId, EstadoPago estadoPago);

    Optional<Pago> findTopByNinoIdOrderByFechaCorteDesc(Long ninoId);

    List<Pago> findByEstado(EstadoPago estado);

    @Query("SELECT p FROM Pago p WHERE p.estado = :estado AND p.fechaCorte < :fechaActual")
    List<Pago> findVencidos(@Param("estado") EstadoPago estado, @Param("fechaActual") LocalDate fechaActual);

    List<Pago> findByNinoIdOrderByFechaCorteDesc(Long ninoId);

    boolean existsByNinoIdAndEstadoAndFechaCorteAfter(Long id, EstadoPago estadoPago, LocalDate fechaCorte);
}
