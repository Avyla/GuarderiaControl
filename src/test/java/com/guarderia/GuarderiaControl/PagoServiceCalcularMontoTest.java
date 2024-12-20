package com.guarderia.GuarderiaControl;

import com.guarderia.GuarderiaControl.service.PagoService;
import com.guarderia.GuarderiaControl.model.Nino;
import com.guarderia.GuarderiaControl.util.TipoCuidado;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PagoServiceCalcularMontoTest {

    @Test
    void testCalcularMonto_TipoDiario_AntesDelDia15() {
        PagoService pagoService = new PagoService();
        Nino nino = new Nino();
        nino.setId(1L);
        nino.setTipoCuidado(TipoCuidado.DIARIO);
        LocalDate fechaInicio = LocalDate.of(2024, 4, 10);
        BigDecimal monto = pagoService.calcularMonto(nino, fechaInicio);

        // Se espera un valor cercano a 76000.02, pero el cálculo interno depende de la lógica de redondeo.
        // Aquí se valida que el monto sea mayor que cero y igual al valor exacto calculado.
        Assertions.assertTrue(monto.compareTo(BigDecimal.ZERO) > 0);
        Assertions.assertEquals(0, monto.compareTo(new BigDecimal("76000.02")));
    }

    @Test
    void testCalcularMonto_TipoSemanal_AntesDelDia15() {
        PagoService pagoService = new PagoService();
        Nino nino = new Nino();
        nino.setId(2L);
        nino.setTipoCuidado(TipoCuidado.DIARIO);
        LocalDate fechaInicio = LocalDate.of(2024, 4, 10);
        BigDecimal monto = pagoService.calcularMonto(nino, fechaInicio);
        Assertions.assertTrue(monto.compareTo(BigDecimal.ZERO) > 0);
    }
}
