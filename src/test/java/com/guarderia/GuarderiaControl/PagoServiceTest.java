package com.guarderia.GuarderiaControl;

import com.guarderia.GuarderiaControl.service.PagoService;
import com.guarderia.GuarderiaControl.util.TipoCuidado;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PagoServiceTest {

    @Test
    void testDeterminarFechaCorte_AntesDelDia15() {
        PagoService pagoService = new PagoService();
        LocalDate fechaReferencia = LocalDate.of(2024, 4, 10);
        LocalDate fechaCorte = pagoService.determinarFechaCorte(fechaReferencia);
        Assertions.assertEquals(LocalDate.of(2024, 4, 15), fechaCorte);
    }

    @Test
    void testDeterminarFechaCorte_DespuesDelDia15() {
        PagoService pagoService = new PagoService();
        LocalDate fechaReferencia = LocalDate.of(2024, 4, 20);
        LocalDate fechaCorte = pagoService.determinarFechaCorte(fechaReferencia);
        Assertions.assertEquals(LocalDate.of(2024, 4, 30), fechaCorte);
    }

    @Test
    void testCalcularNuevaFechaCorte_FechaCorteEs15() {
        PagoService pagoService = new PagoService();
        LocalDate fechaCorteActual = LocalDate.of(2024, 4, 15);
        LocalDate nuevaFecha = pagoService.calcularNuevaFechaCorte(fechaCorteActual);
        Assertions.assertEquals(LocalDate.of(2024, 4, 30), nuevaFecha);
    }

    @Test
    void testCalcularNuevaFechaCorte_FechaCorteNoEs15() {
        PagoService pagoService = new PagoService();
        LocalDate fechaCorteActual = LocalDate.of(2024, 4, 30);
        LocalDate nuevaFecha = pagoService.calcularNuevaFechaCorte(fechaCorteActual);
        Assertions.assertEquals(LocalDate.of(2024, 5, 15), nuevaFecha);
    }
}
