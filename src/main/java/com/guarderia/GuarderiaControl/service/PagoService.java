package com.guarderia.GuarderiaControl.service;

import com.guarderia.GuarderiaControl.model.Nino;
import com.guarderia.GuarderiaControl.model.Pago;
import com.guarderia.GuarderiaControl.repository.NinoRepository;
import com.guarderia.GuarderiaControl.repository.PagoRepository;
import com.guarderia.GuarderiaControl.util.EstadoPago;
import com.guarderia.GuarderiaControl.util.TipoCuidado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    private static final Logger log = LoggerFactory.getLogger(PagoService.class);

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private NinoRepository ninoRepository;

    public LocalDate calcularNuevaFechaCorte(LocalDate fechaCorteActual) {
        log.info("Calculando nueva fecha de corte basada en la fecha de corte actual: {}", fechaCorteActual);

        LocalDate nuevaFechaCorte;
        if (fechaCorteActual.getDayOfMonth() == 15) {
            nuevaFechaCorte = fechaCorteActual.withDayOfMonth(fechaCorteActual.lengthOfMonth());
            log.debug("La fecha de corte actual es el día 15. Nueva fecha de corte ajustada al último día del mes: {}", nuevaFechaCorte);
        } else {
            nuevaFechaCorte = fechaCorteActual.plusMonths(1).withDayOfMonth(15);
            log.debug("La fecha de corte actual no es el día 15. Nueva fecha de corte ajustada al día 15 del siguiente mes: {}", nuevaFechaCorte);
        }

        log.info("Nueva fecha de corte calculada: {}", nuevaFechaCorte);
        return nuevaFechaCorte;
    }

    @Transactional
    public void crearPagoInicial(Nino nino) {
        LocalDate fechaVinculacion = LocalDate.now();
        BigDecimal monto = calcularMonto(nino, fechaVinculacion);
        LocalDate fechaCorte = determinarFechaCorte(fechaVinculacion);

        Pago pago = new Pago();
        pago.setFechaCorte(fechaCorte);
        pago.setMonto(monto.doubleValue());
        pago.setEstado(EstadoPago.POR_PAGAR);
        pago.setNino(nino);

        pagoRepository.save(pago);
    }

    @Transactional
    public BigDecimal calcularMonto(Nino nino, LocalDate fechaInicio) {
        log.info("Iniciando cálculo de monto para el Niño con ID: {} y tipo de cuidado: {}", nino.getId(), nino.getTipoCuidado());
        log.info("Fecha de inicio del cálculo: {}", fechaInicio);

        // Determinar monto mensual según el tipo de cuidado
        BigDecimal montoMensual = nino.getTipoCuidado() == TipoCuidado.DIARIO
                ? BigDecimal.valueOf(380000)
                : BigDecimal.valueOf(220000);
        log.debug("Monto mensual calculado: {}", montoMensual);

        // Calcular costo diario
        BigDecimal costoDiario = montoMensual.divide(BigDecimal.valueOf(30), RoundingMode.HALF_UP);
        log.debug("Costo diario calculado: {}", costoDiario);

        // Determinar la fecha de corte
        LocalDate fechaCorte = determinarFechaCorte(fechaInicio);
        log.info("Fecha de corte determinada: {}", fechaCorte);

        // Calcular los días efectivos
        long diasEfectivos = ChronoUnit.DAYS.between(fechaInicio, fechaCorte.plusDays(1)); // Incluir fecha de corte completa
        log.info("Días efectivos entre la fecha de inicio ({}) y la fecha de corte ({}): {}", fechaInicio, fechaCorte, diasEfectivos);

        // Calcular monto total y redondear a 2 decimales
        BigDecimal montoTotal = (diasEfectivos <= 0) ? BigDecimal.ZERO : costoDiario.multiply(BigDecimal.valueOf(diasEfectivos));
        montoTotal = montoTotal.setScale(2, RoundingMode.HALF_UP); // Redondear a 2 decimales
        log.info("Monto total calculado para el período (redondeado): {}", montoTotal);

        return montoTotal;
    }
    
    public LocalDate determinarFechaCorte(LocalDate fechaReferencia) {
        log.info("Determinando fecha de corte para la fecha de referencia: {}", fechaReferencia);

        LocalDate fechaCorte;
        if (fechaReferencia.getDayOfMonth() <= 15) {
            fechaCorte = fechaReferencia.withDayOfMonth(15);
            log.debug("Fecha de referencia es antes o igual al día 15. Fecha de corte ajustada al 15: {}", fechaCorte);
        } else {
            fechaCorte = fechaReferencia.withDayOfMonth(fechaReferencia.lengthOfMonth());
            log.debug("Fecha de referencia es después del día 15. Fecha de corte ajustada al último día del mes: {}", fechaCorte);
        }

        log.info("Fecha de corte final determinada: {}", fechaCorte);
        return fechaCorte;
    }

    @Scheduled(cron = "0 0 * * * ?") // Todos los días a medianoche
    public void ejecutarProcesamientoPagos() {
        // Procesar pagos pendientes y actualizar estados
        procesarPagosPendientes();

        // Generar nuevos pagos para todos los niños
        List<Nino> ninos = ninoRepository.findAll();
        for (Nino nino : ninos) {
            generarPagoSiguiente(nino);
        }
    }

    public void procesarPagosPendientes() {
        LocalDate fechaActual = LocalDate.now();
        List<Pago> pagosPendientes = pagoRepository.findVencidos(EstadoPago.POR_PAGAR, fechaActual);

        for (Pago pago : pagosPendientes) {
            if (pago.getFechaCorte().isBefore(fechaActual)) {
                pago.setEstado(EstadoPago.VENCIDO);
                log.warn("Pago con ID {} marcado como VENCIDO. Fecha de corte: {}", pago.getId(), pago.getFechaCorte());
            }
        }

        pagoRepository.saveAll(pagosPendientes);
    }

    @Transactional
    public void generarPagoSiguiente(Nino nino) {
        log.info("Generando siguiente pago para el Niño con ID: {}", nino.getId());

        Optional<Pago> ultimoPagoOpt = pagoRepository.findTopByNinoIdOrderByFechaCorteDesc(nino.getId());

        if (ultimoPagoOpt.isEmpty()) {
            log.warn("No se encontró un último pago para el Niño con ID: {}. Saltando generación de pago.", nino.getId());
            return;
        }

        Pago ultimoPago = ultimoPagoOpt.get();
        LocalDate fechaActual = LocalDate.now();

        log.info("Último pago encontrado para el Niño con ID: {} - Fecha de corte: {}", nino.getId(), ultimoPago.getFechaCorte());

        if (fechaActual.isAfter(ultimoPago.getFechaCorte())) {
            LocalDate nuevaFechaCorte = calcularNuevaFechaCorte(ultimoPago.getFechaCorte());
            LocalDate fechaInicio = ultimoPago.getFechaCorte().plusDays(1); // Fecha de inicio un día después de la última fecha de corte
            BigDecimal nuevoMonto = calcularMonto(nino, fechaInicio);

            if (nuevoMonto.compareTo(BigDecimal.ZERO) <= 0) {
                log.warn("El monto calculado es 0 o negativo. Saltando generación de pago para el Niño con ID: {}", nino.getId());
                return;
            }

            log.info("Generando nuevo pago con fecha de corte: {} y monto: {}", nuevaFechaCorte, nuevoMonto);

            Pago nuevoPago = new Pago();
            nuevoPago.setNino(nino);
            nuevoPago.setEstado(EstadoPago.POR_PAGAR);
            nuevoPago.setFechaCorte(nuevaFechaCorte);
            nuevoPago.setMonto(nuevoMonto.doubleValue());

            pagoRepository.save(nuevoPago);
            log.info("Nuevo pago guardado correctamente para el Niño con ID: {}", nino.getId());
        } else {
            log.info("La fecha actual ({}) no supera la fecha de corte ({}) del último pago con ID: {}",
                    fechaActual, ultimoPago.getFechaCorte(), ultimoPago.getId());
        }
    }

    // PagoService.java
    @Transactional
    public void realizarPago(Long pagoId, Double cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a pagar debe ser mayor a 0");
        }

        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        double pendiente = pago.getMontoPendiente();
        if (cantidad > pendiente) {
            throw new IllegalArgumentException("La cantidad a pagar excede el monto pendiente");
        }

        // Actualizar el montoPagado
        pago.setMontoPagado(pago.getMontoPagado() + cantidad);

        // Actualizar estado
        if (pago.getMontoPagado().equals(pago.getMonto())) {
            pago.setEstado(EstadoPago.PAGADO);
        } else if (pago.getEstado() == EstadoPago.VENCIDO && pago.getMontoPagado() < pago.getMonto()) {
            // Si estaba vencido y no se pagó completo,
            // podríamos dejarlo en POR_PAGAR igualmente,
            // ya que se abonó algo. Esto depende de la lógica de negocio.
            pago.setEstado(EstadoPago.POR_PAGAR);
        }

        pagoRepository.save(pago);
    }

}
