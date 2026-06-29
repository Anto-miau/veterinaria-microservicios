package com.veterniaria.ms_citas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.veterniaria.ms_citas.DTO.PagosDTO;
import com.veterniaria.ms_citas.model.Cita;
import com.veterniaria.ms_citas.model.Pago;
import com.veterniaria.ms_citas.model.Pagos;
import com.veterniaria.ms_citas.repository.PagosRepository;

@ExtendWith(MockitoExtension.class)
class PagosServiceTest {

    @Mock
    private PagosRepository pagosRepository;

    @Mock
    private PagosValidaciones pagosValidaciones;

    @InjectMocks
    private PagosService pagosService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscarPorId_Exitoso() {
        Integer idSimulado = 1;
        Cita cita = new Cita();
        cita.setId(1);
        cita.setMotivo("Control");
        Pago pago = new Pago();
        pago.setId(1);
        pago.setNombre("Efectivo");

        Pagos pagosFalso = new Pagos();
        pagosFalso.setId(idSimulado);
        pagosFalso.setCita(cita);
        pagosFalso.setPago(pago);

        PagosDTO dtoEsperado = new PagosDTO();
        dtoEsperado.setId(idSimulado);

        when(pagosRepository.findById(idSimulado)).thenReturn(Optional.of(pagosFalso));
        when(pagosValidaciones.convertirADTO(pagosFalso)).thenReturn(dtoEsperado);

        PagosDTO resultado = pagosService.buscarPorId(idSimulado);

        assertNotNull(resultado);
        verify(pagosRepository, times(1)).findById(idSimulado);
    }

    @Test
    void testBuscarPorId_NoEncontrado() {
        Integer idInexistente = 999;
        when(pagosRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> pagosService.buscarPorId(idInexistente));
        verify(pagosRepository, times(1)).findById(idInexistente);
    }

    @Test
    void testGuardar_Exitoso() {
        Cita cita = new Cita();
        cita.setId(1);
        Pago pago = new Pago();
        pago.setId(1);

        Pagos pagosValido = new Pagos();
        pagosValido.setCita(cita);
        pagosValido.setPago(pago);

        PagosDTO dtoEsperado = new PagosDTO();
        dtoEsperado.setId(1);

        when(pagosValidaciones.validarNullVacio(pagosValido)).thenReturn(true);
        when(pagosRepository.save(pagosValido)).thenReturn(pagosValido);
        when(pagosValidaciones.convertirADTO(pagosValido)).thenReturn(dtoEsperado);

        PagosDTO resultado = pagosService.guardar(pagosValido);

        assertNotNull(resultado);
        verify(pagosRepository, times(1)).save(pagosValido);
    }

    @Test
    void testGuardar_ValidacionFallida() {
        Pagos pagosInvalido = new Pagos();

        when(pagosValidaciones.validarNullVacio(pagosInvalido)).thenReturn(false);

        PagosDTO resultado = pagosService.guardar(pagosInvalido);

        assertNull(resultado);
        verify(pagosRepository, times(0)).save(pagosInvalido);
    }

    @Test
    void testEliminar_Exitoso() {
        Integer idSimulado = 5;
        Pagos pagosFalso = new Pagos();
        pagosFalso.setId(idSimulado);

        when(pagosRepository.findById(idSimulado)).thenReturn(Optional.of(pagosFalso));

        String resultado = pagosService.eliminar(idSimulado);

        assertNotNull(resultado);
        assertEquals(true, resultado.contains("eliminada"));
        verify(pagosRepository, times(1)).delete(pagosFalso);
    }
}