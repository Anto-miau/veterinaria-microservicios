package com.veterniaria.ms_mascotas.service;

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

import com.veterniaria.ms_mascotas.DTO.EnfermedadesDTO;
import com.veterniaria.ms_mascotas.model.Enfermedad;
import com.veterniaria.ms_mascotas.model.Enfermedades;
import com.veterniaria.ms_mascotas.model.Mascota;
import com.veterniaria.ms_mascotas.repository.EnfermedadesRepository;

@ExtendWith(MockitoExtension.class)
class EnfermedadesServiceTest {

    @Mock
    private EnfermedadesRepository enfermedadesRepository;

    @Mock
    private EnfermedadesValidaciones enfermedadesValidaciones;

    @InjectMocks
    private EnfermedadesService enfermedadesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscarPorId_Exitoso() {
        Integer idSimulado = 1;
        Mascota mascota = new Mascota();
        mascota.setId(1);
        mascota.setNombre("Firulais");
        Enfermedad enfermedad = new Enfermedad();
        enfermedad.setId(1);
        enfermedad.setNombre("Rabia");

        Enfermedades enfermedadesFalso = new Enfermedades();
        enfermedadesFalso.setId(idSimulado);
        enfermedadesFalso.setMascota(mascota);
        enfermedadesFalso.setEnfermedad(enfermedad);

        EnfermedadesDTO dtoEsperado = new EnfermedadesDTO();
        dtoEsperado.setId(idSimulado);

        when(enfermedadesRepository.findById(idSimulado)).thenReturn(Optional.of(enfermedadesFalso));
        when(enfermedadesValidaciones.convertirADTO(enfermedadesFalso)).thenReturn(dtoEsperado);

        EnfermedadesDTO resultado = enfermedadesService.buscarPorId(idSimulado);

        assertNotNull(resultado);
        verify(enfermedadesRepository, times(1)).findById(idSimulado);
    }

    @Test
    void testBuscarPorId_NoEncontrado() {
        Integer idInexistente = 999;
        when(enfermedadesRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> enfermedadesService.buscarPorId(idInexistente));
        verify(enfermedadesRepository, times(1)).findById(idInexistente);
    }

    @Test
    void testGuardar_Exitoso() {
        Mascota mascota = new Mascota();
        mascota.setId(1);
        Enfermedad enfermedad = new Enfermedad();
        enfermedad.setId(1);

        Enfermedades enfermedadesValido = new Enfermedades();
        enfermedadesValido.setMascota(mascota);
        enfermedadesValido.setEnfermedad(enfermedad);

        EnfermedadesDTO dtoEsperado = new EnfermedadesDTO();
        dtoEsperado.setId(1);

        when(enfermedadesValidaciones.validarNullVacio(enfermedadesValido)).thenReturn(true);
        when(enfermedadesRepository.save(enfermedadesValido)).thenReturn(enfermedadesValido);
        when(enfermedadesValidaciones.convertirADTO(enfermedadesValido)).thenReturn(dtoEsperado);

        EnfermedadesDTO resultado = enfermedadesService.guardar(enfermedadesValido);

        assertNotNull(resultado);
        verify(enfermedadesRepository, times(1)).save(enfermedadesValido);
    }

    @Test
    void testGuardar_ValidacionFallida() {
        Enfermedades enfermedadesInvalido = new Enfermedades();
        // sin mascota ni enfermedad asignados

        when(enfermedadesValidaciones.validarNullVacio(enfermedadesInvalido)).thenReturn(false);

        EnfermedadesDTO resultado = enfermedadesService.guardar(enfermedadesInvalido);

        assertNull(resultado);
        verify(enfermedadesRepository, times(0)).save(enfermedadesInvalido);
    }

    @Test
    void testEliminar_Exitoso() {
        Integer idSimulado = 5;
        Enfermedades enfermedadesFalso = new Enfermedades();
        enfermedadesFalso.setId(idSimulado);

        when(enfermedadesRepository.findById(idSimulado)).thenReturn(Optional.of(enfermedadesFalso));

        String resultado = enfermedadesService.eliminar(idSimulado);

        assertNotNull(resultado);
        assertEquals(true, resultado.contains("eliminada"));
        verify(enfermedadesRepository, times(1)).delete(enfermedadesFalso);
    }
}