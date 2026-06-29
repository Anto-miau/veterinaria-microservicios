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

import com.veterniaria.ms_citas.DTO.TratamientosDTO;
import com.veterniaria.ms_citas.model.Cita;
import com.veterniaria.ms_citas.model.Tratamiento;
import com.veterniaria.ms_citas.model.Tratamientos;
import com.veterniaria.ms_citas.repository.TratamientosRepository;

@ExtendWith(MockitoExtension.class)
class TratamientosServiceTest {

    @Mock
    private TratamientosRepository tratamientosRepository;

    @Mock
    private TratamientosValidaciones tratamientosValidaciones;

    @InjectMocks
    private TratamientosService tratamientosService;

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
        Tratamiento tratamiento = new Tratamiento();
        tratamiento.setId(1);
        tratamiento.setDiagnostico("Otitis");

        Tratamientos tratamientosFalso = new Tratamientos();
        tratamientosFalso.setId(idSimulado);
        tratamientosFalso.setCita(cita);
        tratamientosFalso.setTratamiento(tratamiento);

        TratamientosDTO dtoEsperado = new TratamientosDTO();
        dtoEsperado.setId(idSimulado);

        when(tratamientosRepository.findById(idSimulado)).thenReturn(Optional.of(tratamientosFalso));
        when(tratamientosValidaciones.convertirADTO(tratamientosFalso)).thenReturn(dtoEsperado);

        TratamientosDTO resultado = tratamientosService.buscarPorId(idSimulado);

        assertNotNull(resultado);
        verify(tratamientosRepository, times(1)).findById(idSimulado);
    }

    @Test
    void testBuscarPorId_NoEncontrado() {
        Integer idInexistente = 999;
        when(tratamientosRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> tratamientosService.buscarPorId(idInexistente));
        verify(tratamientosRepository, times(1)).findById(idInexistente);
    }

    @Test
    void testGuardar_Exitoso() {
        Cita cita = new Cita();
        cita.setId(1);
        Tratamiento tratamiento = new Tratamiento();
        tratamiento.setId(1);

        Tratamientos tratamientosValido = new Tratamientos();
        tratamientosValido.setCita(cita);
        tratamientosValido.setTratamiento(tratamiento);

        TratamientosDTO dtoEsperado = new TratamientosDTO();
        dtoEsperado.setId(1);

        when(tratamientosValidaciones.validarNullVacio(tratamientosValido)).thenReturn(true);
        when(tratamientosRepository.save(tratamientosValido)).thenReturn(tratamientosValido);
        when(tratamientosValidaciones.convertirADTO(tratamientosValido)).thenReturn(dtoEsperado);

        TratamientosDTO resultado = tratamientosService.guardar(tratamientosValido);

        assertNotNull(resultado);
        verify(tratamientosRepository, times(1)).save(tratamientosValido);
    }

    @Test
    void testGuardar_ValidacionFallida() {
        Tratamientos tratamientosInvalido = new Tratamientos();

        when(tratamientosValidaciones.validarNullVacio(tratamientosInvalido)).thenReturn(false);

        TratamientosDTO resultado = tratamientosService.guardar(tratamientosInvalido);

        assertNull(resultado);
        verify(tratamientosRepository, times(0)).save(tratamientosInvalido);
    }

    @Test
    void testEliminar_Exitoso() {
        Integer idSimulado = 5;
        Tratamientos tratamientosFalso = new Tratamientos();
        tratamientosFalso.setId(idSimulado);

        when(tratamientosRepository.findById(idSimulado)).thenReturn(Optional.of(tratamientosFalso));

        String resultado = tratamientosService.eliminar(idSimulado);

        assertNotNull(resultado);
        assertEquals(true, resultado.contains("eliminada"));
        verify(tratamientosRepository, times(1)).delete(tratamientosFalso);
    }
}