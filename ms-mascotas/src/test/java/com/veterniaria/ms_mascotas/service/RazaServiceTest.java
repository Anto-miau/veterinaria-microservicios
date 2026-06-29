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

import com.veterniaria.ms_mascotas.DTO.RazaDTO;
import com.veterniaria.ms_mascotas.model.Especie;
import com.veterniaria.ms_mascotas.model.Raza;
import com.veterniaria.ms_mascotas.repository.RazaRepository;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class RazaServiceTest {

    @Mock
    private RazaRepository razaRepository;

    @Mock
    private RazaValidaciones razaValidaciones;

    @InjectMocks
    private RazaService razaService;

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscarPorId_Exitoso() {
        Integer idSimulado = 1;
        String nombreAleatorio = faker.dog().breed();
        Especie especie = new Especie();
        especie.setId(1);
        especie.setNombre("Canino");

        Raza razaFalsa = new Raza();
        razaFalsa.setId(idSimulado);
        razaFalsa.setNombre(nombreAleatorio);
        razaFalsa.setEspecie(especie);

        RazaDTO dtoEsperado = new RazaDTO();
        dtoEsperado.setId(idSimulado);
        dtoEsperado.setNombre(nombreAleatorio);

        when(razaRepository.findById(idSimulado)).thenReturn(Optional.of(razaFalsa));
        when(razaValidaciones.convertirADTO(razaFalsa)).thenReturn(dtoEsperado);

        RazaDTO resultado = razaService.buscarPorId(idSimulado);

        assertNotNull(resultado);
        assertEquals(nombreAleatorio, resultado.getNombre());
        verify(razaRepository, times(1)).findById(idSimulado);
    }

    @Test
    void testBuscarPorId_NoEncontrado() {
        Integer idInexistente = 999;
        when(razaRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> razaService.buscarPorId(idInexistente));
        verify(razaRepository, times(1)).findById(idInexistente);
    }

    @Test
    void testGuardar_Exitoso() {
        Especie especie = new Especie();
        especie.setId(1);
        especie.setNombre("Felino");

        Raza razaValida = new Raza();
        razaValida.setNombre(faker.dog().breed());
        razaValida.setEspecie(especie);

        RazaDTO dtoEsperado = new RazaDTO();
        dtoEsperado.setNombre(razaValida.getNombre());

        when(razaValidaciones.validarNullVacio(razaValida)).thenReturn(true);
        when(razaRepository.save(razaValida)).thenReturn(razaValida);
        when(razaValidaciones.convertirADTO(razaValida)).thenReturn(dtoEsperado);

        RazaDTO resultado = razaService.guardar(razaValida);

        assertNotNull(resultado);
        assertEquals(razaValida.getNombre(), resultado.getNombre());
        verify(razaRepository, times(1)).save(razaValida);
    }

    @Test
    void testGuardar_ValidacionFallida() {
        Raza razaInvalida = new Raza();
        razaInvalida.setNombre("");

        when(razaValidaciones.validarNullVacio(razaInvalida)).thenReturn(false);

        RazaDTO resultado = razaService.guardar(razaInvalida);

        assertNull(resultado);
        verify(razaRepository, times(0)).save(razaInvalida);
    }

    @Test
    void testEliminar_Exitoso() {
        Integer idSimulado = 5;
        Raza razaFalsa = new Raza();
        razaFalsa.setId(idSimulado);
        razaFalsa.setNombre(faker.dog().breed());

        when(razaRepository.findById(idSimulado)).thenReturn(Optional.of(razaFalsa));

        String resultado = razaService.eliminar(idSimulado);

        assertNotNull(resultado);
        assertEquals(true, resultado.contains("exitosamente"));
        verify(razaRepository, times(1)).delete(razaFalsa);
    }
}
