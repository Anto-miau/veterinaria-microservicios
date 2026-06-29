package com.veterniaria.ms_citas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.veterniaria.ms_citas.DTO.TratamientoDTO;
import com.veterniaria.ms_citas.model.Tratamiento;
import com.veterniaria.ms_citas.repository.TratamientoRepository;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class TratamientoServiceTest {

    @Mock
    private TratamientoRepository tratamientoRepository;

    @Mock
    private TratamientoValidaciones tratamientoValidaciones;

    @InjectMocks
    private TratamientoService tratamientoService;

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscarPorId_Exitoso() {
        Integer idSimulado = 1;
        String diagnosticoAleatorio = faker.lorem().sentence();
        Tratamiento tratamientoFalso = new Tratamiento();
        tratamientoFalso.setId(idSimulado);
        tratamientoFalso.setDiagnostico(diagnosticoAleatorio);
        tratamientoFalso.setDescripcion("Descripción de prueba");
        tratamientoFalso.setFechaInicio(new Date());
        tratamientoFalso.setFechaTermino(new Date());

        TratamientoDTO dtoEsperado = new TratamientoDTO();
        dtoEsperado.setId(idSimulado);
        dtoEsperado.setDiagnostico(diagnosticoAleatorio);

        when(tratamientoRepository.findById(idSimulado)).thenReturn(Optional.of(tratamientoFalso));
        when(tratamientoValidaciones.convertirADTO(tratamientoFalso)).thenReturn(dtoEsperado);

        TratamientoDTO resultado = tratamientoService.buscarPorId(idSimulado);

        assertNotNull(resultado);
        assertEquals(diagnosticoAleatorio, resultado.getDiagnostico());
        verify(tratamientoRepository, times(1)).findById(idSimulado);
    }

    @Test
    void testBuscarPorId_NoEncontrado() {
        Integer idInexistente = 999;
        when(tratamientoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> tratamientoService.buscarPorId(idInexistente));
        verify(tratamientoRepository, times(1)).findById(idInexistente);
    }

    @Test
    void testGuardar_Exitoso() {
        Tratamiento tratamientoValido = new Tratamiento();
        tratamientoValido.setDiagnostico(faker.lorem().sentence());
        tratamientoValido.setDescripcion("Descripción válida");
        tratamientoValido.setFechaInicio(new Date());
        tratamientoValido.setFechaTermino(new Date());

        TratamientoDTO dtoEsperado = new TratamientoDTO();
        dtoEsperado.setDiagnostico(tratamientoValido.getDiagnostico());

        when(tratamientoValidaciones.validarNullVacio(tratamientoValido)).thenReturn(true);
        when(tratamientoRepository.save(tratamientoValido)).thenReturn(tratamientoValido);
        when(tratamientoValidaciones.convertirADTO(tratamientoValido)).thenReturn(dtoEsperado);

        TratamientoDTO resultado = tratamientoService.guardar(tratamientoValido);

        assertNotNull(resultado);
        assertEquals(tratamientoValido.getDiagnostico(), resultado.getDiagnostico());
        verify(tratamientoRepository, times(1)).save(tratamientoValido);
    }

    @Test
    void testGuardar_ValidacionFallida() {
        Tratamiento tratamientoInvalido = new Tratamiento();
        tratamientoInvalido.setDiagnostico("");

        when(tratamientoValidaciones.validarNullVacio(tratamientoInvalido)).thenReturn(false);

        TratamientoDTO resultado = tratamientoService.guardar(tratamientoInvalido);

        assertNull(resultado);
        verify(tratamientoRepository, times(0)).save(tratamientoInvalido);
    }

    @Test
    void testEliminar_Exitoso() {
        Integer idSimulado = 5;
        Tratamiento tratamientoFalso = new Tratamiento();
        tratamientoFalso.setId(idSimulado);
        tratamientoFalso.setDiagnostico(faker.lorem().sentence());

        when(tratamientoRepository.findById(idSimulado)).thenReturn(Optional.of(tratamientoFalso));

        String resultado = tratamientoService.eliminar(idSimulado);

        assertNotNull(resultado);
        assertEquals(true, resultado.contains("exitosamente"));
        verify(tratamientoRepository, times(1)).delete(tratamientoFalso);
    }
}