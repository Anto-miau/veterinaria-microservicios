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

import com.veterniaria.ms_mascotas.DTO.EnfermedadDTO;
import com.veterniaria.ms_mascotas.model.Enfermedad;
import com.veterniaria.ms_mascotas.repository.EnfermedadRepository;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class EnfermedadServiceTest {

    @Mock
    private EnfermedadRepository enfermedadRepository;

    @Mock
    private EnfermedadValidaciones enfermedadValidaciones;

    @InjectMocks
    private EnfermedadService enfermedadService;

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscarPorId_Exitoso() {
        Integer idSimulado = 1;
        String nombreAleatorio = faker.medical().diseaseName();
        Enfermedad enfermedadFalsa = new Enfermedad();
        enfermedadFalsa.setId(idSimulado);
        enfermedadFalsa.setNombre(nombreAleatorio);
        enfermedadFalsa.setDescripcion("Descripción de prueba");

        EnfermedadDTO dtoEsperado = new EnfermedadDTO();
        dtoEsperado.setId(idSimulado);
        dtoEsperado.setNombre(nombreAleatorio);

        when(enfermedadRepository.findById(idSimulado)).thenReturn(Optional.of(enfermedadFalsa));
        when(enfermedadValidaciones.convertirADTO(enfermedadFalsa)).thenReturn(dtoEsperado);

        EnfermedadDTO resultado = enfermedadService.buscarPorId(idSimulado);

        assertNotNull(resultado);
        assertEquals(nombreAleatorio, resultado.getNombre());
        verify(enfermedadRepository, times(1)).findById(idSimulado);
    }

    @Test
    void testBuscarPorId_NoEncontrado() {
        Integer idInexistente = 999;
        when(enfermedadRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> enfermedadService.buscarPorId(idInexistente));
        verify(enfermedadRepository, times(1)).findById(idInexistente);
    }

    @Test
    void testGuardar_Exitoso() {
        Enfermedad enfermedadValida = new Enfermedad();
        enfermedadValida.setNombre(faker.medical().diseaseName());
        enfermedadValida.setDescripcion("Descripción válida de prueba");

        EnfermedadDTO dtoEsperado = new EnfermedadDTO();
        dtoEsperado.setNombre(enfermedadValida.getNombre());

        when(enfermedadValidaciones.validarNullVacio(enfermedadValida)).thenReturn(true);
        when(enfermedadRepository.save(enfermedadValida)).thenReturn(enfermedadValida);
        when(enfermedadValidaciones.convertirADTO(enfermedadValida)).thenReturn(dtoEsperado);

        EnfermedadDTO resultado = enfermedadService.guardar(enfermedadValida);

        assertNotNull(resultado);
        assertEquals(enfermedadValida.getNombre(), resultado.getNombre());
        verify(enfermedadRepository, times(1)).save(enfermedadValida);
    }

    @Test
    void testGuardar_ValidacionFallida() {
        Enfermedad enfermedadInvalida = new Enfermedad();
        enfermedadInvalida.setNombre("");

        when(enfermedadValidaciones.validarNullVacio(enfermedadInvalida)).thenReturn(false);

        EnfermedadDTO resultado = enfermedadService.guardar(enfermedadInvalida);

        assertNull(resultado);
        verify(enfermedadRepository, times(0)).save(enfermedadInvalida);
    }

    @Test
    void testEliminar_Exitoso() {
        Integer idSimulado = 5;
        Enfermedad enfermedadFalsa = new Enfermedad();
        enfermedadFalsa.setId(idSimulado);
        enfermedadFalsa.setNombre(faker.medical().diseaseName());

        when(enfermedadRepository.findById(idSimulado)).thenReturn(Optional.of(enfermedadFalsa));

        String resultado = enfermedadService.eliminar(idSimulado);

        assertNotNull(resultado);
        assertEquals(true, resultado.contains("exitosamente"));
        verify(enfermedadRepository, times(1)).delete(enfermedadFalsa);
    }
}
