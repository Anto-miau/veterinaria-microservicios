package com.veterniaria.ms_citas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.veterniaria.ms_citas.DTO.CitaDTO;
import com.veterniaria.ms_citas.model.Cita;
import com.veterniaria.ms_citas.repository.CitaRepository;

@ExtendWith(MockitoExtension.class)
class CitaServiceTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private CitaValidaciones citaValidaciones;

    @InjectMocks
    private CitaService citaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscarPorId_Exitoso() {
        Integer idSimulado = 1;
        String motivoSimulado = "Vacunación anual";
        Cita citaFalsa = new Cita();
        citaFalsa.setId(idSimulado);
        citaFalsa.setMotivo(motivoSimulado);
        citaFalsa.setFechaHora(LocalDateTime.now());
        citaFalsa.setEstado("PENDIENTE");
        citaFalsa.setMascotaId(1);

        CitaDTO dtoEsperado = new CitaDTO();
        dtoEsperado.setId(idSimulado);
        dtoEsperado.setMotivo(motivoSimulado);

        when(citaRepository.findById(idSimulado)).thenReturn(Optional.of(citaFalsa));
        when(citaValidaciones.convertirADTO(citaFalsa)).thenReturn(dtoEsperado);

        CitaDTO resultado = citaService.buscarPorId(idSimulado);

        assertNotNull(resultado);
        assertEquals(motivoSimulado, resultado.getMotivo());
        verify(citaRepository, times(1)).findById(idSimulado);
    }

    @Test
    void testBuscarPorId_NoEncontrado() {
        Integer idInexistente = 999;
        when(citaRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> citaService.buscarPorId(idInexistente));
        verify(citaRepository, times(1)).findById(idInexistente);
    }

    @Test
    void testGuardar_Exitoso() {
        Cita citaValida = new Cita();
        citaValida.setMotivo("Control de rutina");
        citaValida.setFechaHora(LocalDateTime.now());
        citaValida.setEstado("PENDIENTE");
        citaValida.setMascotaId(1);

        CitaDTO dtoEsperado = new CitaDTO();
        dtoEsperado.setMotivo(citaValida.getMotivo());

        when(citaValidaciones.validarNullVacio(citaValida)).thenReturn(true);
        when(citaRepository.save(citaValida)).thenReturn(citaValida);
        when(citaValidaciones.convertirADTO(citaValida)).thenReturn(dtoEsperado);

        CitaDTO resultado = citaService.guardar(citaValida);

        assertNotNull(resultado);
        assertEquals(citaValida.getMotivo(), resultado.getMotivo());
        verify(citaRepository, times(1)).save(citaValida);
    }

    @Test
    void testGuardar_ValidacionFallida() {
        Cita citaInvalida = new Cita();
        citaInvalida.setMotivo("");

        when(citaValidaciones.validarNullVacio(citaInvalida)).thenReturn(false);

        CitaDTO resultado = citaService.guardar(citaInvalida);

        assertNull(resultado);
        verify(citaRepository, times(0)).save(citaInvalida);
    }

    @Test
    void testEliminar_Exitoso() {
        Integer idSimulado = 5;
        Cita citaFalsa = new Cita();
        citaFalsa.setId(idSimulado);
        citaFalsa.setMotivo("Cirugía menor");

        when(citaRepository.findById(idSimulado)).thenReturn(Optional.of(citaFalsa));

        String resultado = citaService.eliminar(idSimulado);

        assertNotNull(resultado);
        assertEquals(true, resultado.contains("exitosamente"));
        verify(citaRepository, times(1)).delete(citaFalsa);
    }
}