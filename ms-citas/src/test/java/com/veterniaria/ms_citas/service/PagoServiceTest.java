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

import com.veterniaria.ms_citas.DTO.PagoDTO;
import com.veterniaria.ms_citas.model.Pago;
import com.veterniaria.ms_citas.repository.PagoRepository;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private PagoValidaciones pagoValidaciones;

    @InjectMocks
    private PagoService pagoService;

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscarPorId_Exitoso() {
        Integer idSimulado = 1;
        String nombreAleatorio = faker.finance().creditCard();
        Pago pagoFalso = new Pago();
        pagoFalso.setId(idSimulado);
        pagoFalso.setNombre(nombreAleatorio);

        PagoDTO dtoEsperado = new PagoDTO();
        dtoEsperado.setId(idSimulado);
        dtoEsperado.setNombre(nombreAleatorio);

        when(pagoRepository.findById(idSimulado)).thenReturn(Optional.of(pagoFalso));
        when(pagoValidaciones.convertirADTO(pagoFalso)).thenReturn(dtoEsperado);

        PagoDTO resultado = pagoService.buscarPorId(idSimulado);

        assertNotNull(resultado);
        assertEquals(nombreAleatorio, resultado.getNombre());
        verify(pagoRepository, times(1)).findById(idSimulado);
    }

    @Test
    void testBuscarPorId_NoEncontrado() {
        Integer idInexistente = 999;
        when(pagoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> pagoService.buscarPorId(idInexistente));
        verify(pagoRepository, times(1)).findById(idInexistente);
    }

    @Test
    void testGuardar_Exitoso() {
        Pago pagoValido = new Pago();
        pagoValido.setNombre("Efectivo");

        PagoDTO dtoEsperado = new PagoDTO();
        dtoEsperado.setNombre(pagoValido.getNombre());

        when(pagoValidaciones.validarNullVacio(pagoValido)).thenReturn(true);
        when(pagoRepository.save(pagoValido)).thenReturn(pagoValido);
        when(pagoValidaciones.convertirADTO(pagoValido)).thenReturn(dtoEsperado);

        PagoDTO resultado = pagoService.guardar(pagoValido);

        assertNotNull(resultado);
        assertEquals(pagoValido.getNombre(), resultado.getNombre());
        verify(pagoRepository, times(1)).save(pagoValido);
    }

    @Test
    void testGuardar_ValidacionFallida() {
        Pago pagoInvalido = new Pago();
        pagoInvalido.setNombre("");

        when(pagoValidaciones.validarNullVacio(pagoInvalido)).thenReturn(false);

        PagoDTO resultado = pagoService.guardar(pagoInvalido);

        assertNull(resultado);
        verify(pagoRepository, times(0)).save(pagoInvalido);
    }

    @Test
    void testEliminar_Exitoso() {
        Integer idSimulado = 5;
        Pago pagoFalso = new Pago();
        pagoFalso.setId(idSimulado);
        pagoFalso.setNombre("Tarjeta");

        when(pagoRepository.findById(idSimulado)).thenReturn(Optional.of(pagoFalso));

        String resultado = pagoService.eliminar(idSimulado);

        assertNotNull(resultado);
        assertEquals(true, resultado.contains("exitosamente"));
        verify(pagoRepository, times(1)).delete(pagoFalso);
    }
}