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

import com.veterniaria.ms_mascotas.DTO.EspecieDTO;
import com.veterniaria.ms_mascotas.model.Especie;
import com.veterniaria.ms_mascotas.repository.EspecieRepository;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class EspecieServiceTest {

    @Mock
    private EspecieRepository especieRepository;

    @Mock
    private EspecieValidaciones especieValidaciones;

    @InjectMocks
    private EspecieService especieService;

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscarPorId_Exitoso() {
        Integer idSimulado = 1;
        String nombreAleatorio = faker.animal().name();
        Especie especieFalsa = new Especie();
        especieFalsa.setId(idSimulado);
        especieFalsa.setNombre(nombreAleatorio);

        EspecieDTO dtoEsperado = new EspecieDTO();
        dtoEsperado.setId(idSimulado);
        dtoEsperado.setNombre(nombreAleatorio);

        when(especieRepository.findById(idSimulado)).thenReturn(Optional.of(especieFalsa));
        when(especieValidaciones.convertirADTO(especieFalsa)).thenReturn(dtoEsperado);

        EspecieDTO resultado = especieService.buscarPorId(idSimulado);

        assertNotNull(resultado);
        assertEquals(nombreAleatorio, resultado.getNombre());
        verify(especieRepository, times(1)).findById(idSimulado);
    }

    @Test
    void testBuscarPorId_NoEncontrado() {
        Integer idInexistente = 999;
        when(especieRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> especieService.buscarPorId(idInexistente));
        verify(especieRepository, times(1)).findById(idInexistente);
    }

    @Test
    void testGuardar_Exitoso() {
        Especie especieValida = new Especie();
        especieValida.setNombre(faker.animal().name());

        EspecieDTO dtoEsperado = new EspecieDTO();
        dtoEsperado.setNombre(especieValida.getNombre());

        when(especieValidaciones.validarNullVacio(especieValida)).thenReturn(true);
        when(especieRepository.save(especieValida)).thenReturn(especieValida);
        when(especieValidaciones.convertirADTO(especieValida)).thenReturn(dtoEsperado);

        EspecieDTO resultado = especieService.guardar(especieValida);

        assertNotNull(resultado);
        assertEquals(especieValida.getNombre(), resultado.getNombre());
        verify(especieRepository, times(1)).save(especieValida);
    }

    @Test
    void testGuardar_ValidacionFallida() {
        Especie especieInvalida = new Especie();
        especieInvalida.setNombre("");

        when(especieValidaciones.validarNullVacio(especieInvalida)).thenReturn(false);

        EspecieDTO resultado = especieService.guardar(especieInvalida);

        assertNull(resultado);
        verify(especieRepository, times(0)).save(especieInvalida);
    }

    @Test
    void testEliminar_Exitoso() {
        Integer idSimulado = 5;
        Especie especieFalsa = new Especie();
        especieFalsa.setId(idSimulado);
        especieFalsa.setNombre(faker.animal().name());

        when(especieRepository.findById(idSimulado)).thenReturn(Optional.of(especieFalsa));

        String resultado = especieService.eliminar(idSimulado);

        assertNotNull(resultado);
        assertEquals(true, resultado.contains("exitosamente"));
        verify(especieRepository, times(1)).delete(especieFalsa);
    }
}