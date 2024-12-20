package com.guarderia.GuarderiaControl;

import com.guarderia.GuarderiaControl.model.Nino;
import com.guarderia.GuarderiaControl.model.Pago;
import com.guarderia.GuarderiaControl.repository.PagoRepository;
import com.guarderia.GuarderiaControl.repository.NinoRepository;
import com.guarderia.GuarderiaControl.service.PagoService;
import com.guarderia.GuarderiaControl.util.EstadoPago;
import com.guarderia.GuarderiaControl.util.TipoCuidado;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PagoServiceWithMocksTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private NinoRepository ninoRepository;

    @InjectMocks
    private PagoService pagoService;

    private Nino nino;
    private Pago ultimoPago;

    @BeforeEach
    void setUp() {
        nino = new Nino();
        nino.setId(1L);
        nino.setTipoCuidado(TipoCuidado.DIARIO);

        ultimoPago = new Pago();
        ultimoPago.setId(100L);
        ultimoPago.setNino(nino);
        ultimoPago.setEstado(EstadoPago.POR_PAGAR);
        ultimoPago.setFechaCorte(LocalDate.now().minusDays(1));
        ultimoPago.setMonto(30000.0);
    }

    @Test
    void testGenerarPagoSiguiente() {
        when(pagoRepository.findTopByNinoIdOrderByFechaCorteDesc(nino.getId()))
                .thenReturn(Optional.of(ultimoPago));

        pagoService.generarPagoSiguiente(nino);

        ArgumentCaptor<Pago> captor = ArgumentCaptor.forClass(Pago.class);
        verify(pagoRepository).save(captor.capture());
        Pago nuevoPago = captor.getValue();

        // Se verifica que el nuevo pago tenga el estado POR_PAGAR, el niño asociado.
        Assertions.assertEquals(EstadoPago.POR_PAGAR, nuevoPago.getEstado());
        Assertions.assertEquals(nino, nuevoPago.getNino());
        Assertions.assertTrue(nuevoPago.getMonto() > 0);
        Assertions.assertNotNull(nuevoPago.getFechaCorte());
    }
}
