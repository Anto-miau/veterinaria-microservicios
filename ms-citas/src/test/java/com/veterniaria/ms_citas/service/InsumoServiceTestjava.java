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

import com.veterniaria.ms_citas.DTO.InsumoDTO;
import com.veterniaria.ms_citas.model.Insumo;
import com.veterniaria.ms_citas.repository.InsumoRepository;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class InsumoServiceTest {

    @Mock
    private InsumoRepository insumoRepository;

    @Mock
    private InsumoValidaciones insumoValidaciones;

    @InjectMocks
    private InsumoService insumoService;

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscarPorId_Exitoso() {
        Integer idSimulado = 1;
        String nombreAleatorio = faker.commerce().productName();
        Insumo insumoFalso = new Insumo();
        insumoFalso.setId(idSimulado);
        insumoFalso.setNombre(nombreAleatorio);
        insumoFalso.setDescripcion("Descripción de prueba");
        insumoFalso.setStock(faker.number().numberBetween(0, 100));

        InsumoDTO dtoEsperado = new InsumoDTO();
        dtoEsperado.setId(idSimulado);
        dtoEsperado.setNombre(nombreAleatorio);

        when(insumoRepository.findById(idSimulado)).thenReturn(Optional.of(insumoFalso));
        when(insumoValidaciones.convertirADTO(insumoFalso)).thenReturn(dtoEsperado);

        InsumoDTO resultado = insumoService.buscarPorId(idSimulado);

        assertNotNull(resultado);
        assertEquals(nombreAleatorio, resultado.getNombre());
        verify(insumoRepository, times(1)).findById(idSimulado);
    }

    @Test
    void testBuscarPorId_NoEncontrado() {
        Integer idInexistente = 999;
        when(insumoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> insumoService.buscarPorId(idInexistente));
        verify(insumoRepository, times(1)).findById(idInexistente);
    }

    @Test
    void testGuardar_Exitoso() {
        Insumo insumoValido = new Insumo();
        insumoValido.setNombre(faker.commerce().productName());
        insumoValido.setDescripcion("Descripción válida");
        insumoValido.setStock(faker.number().numberBetween(0, 100));

        InsumoDTO dtoEsperado = new InsumoDTO();
        dtoEsperado.setNombre(insumoValido.getNombre());

        when(insumoValidaciones.validarNullVacio(insumoValido)).thenReturn(true);
        when(insumoRepository.save(insumoValido)).thenReturn(insumoValido);
        when(insumoValidaciones.convertirADTO(insumoValido)).thenReturn(dtoEsperado);

        InsumoDTO resultado = insumoService.guardar(insumoValido);

        assertNotNull(resultado);
        assertEquals(insumoValido.getNombre(), resultado.getNombre());
        verify(insumoRepository, times(1)).save(insumoValido);
    }

    @Test
    void testGuardar_ValidacionFallida() {
        Insumo insumoInvalido = new Insumo();
        insumoInvalido.setNombre("");

        when(insumoValidaciones.validarNullVacio(insumoInvalido)).thenReturn(false);

        InsumoDTO resultado = insumoService.guardar(insumoInvalido);

        assertNull(resultado);
        verify(insumoRepository, times(0)).save(insumoInvalido);
    }

    @Test
    void testEliminar_Exitoso() {
        Integer idSimulado = 5;
        Insumo insumoFalso = new Insumo();
        insumoFalso.setId(idSimulado);
        insumoFalso.setNombre(faker.commerce().productName());

        when(insumoRepository.findById(idSimulado)).thenReturn(Optional.of(insumoFalso));

        String resultado = insumoService.eliminar(idSimulado);

        assertNotNull(resultado);
        assertEquals(true, resultado.contains("exitosamente"));
        verify(insumoRepository, times(1)).delete(insumoFalso);
    }
}