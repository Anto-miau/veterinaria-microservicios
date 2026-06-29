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

import com.veterniaria.ms_citas.DTO.InsumosDTO;
import com.veterniaria.ms_citas.model.Insumo;
import com.veterniaria.ms_citas.model.Insumos;
import com.veterniaria.ms_citas.model.Tratamiento;
import com.veterniaria.ms_citas.repository.InsumosRepository;

@ExtendWith(MockitoExtension.class)
class InsumosServiceTest {

    @Mock
    private InsumosRepository insumosRepository;

    @Mock
    private InsumosValidaciones insumosValidaciones;

    @InjectMocks
    private InsumosService insumosService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscarPorId_Exitoso() {
        Integer idSimulado = 1;
        Tratamiento tratamiento = new Tratamiento();
        tratamiento.setId(1);
        tratamiento.setDiagnostico("Otitis");
        Insumo insumo = new Insumo();
        insumo.setId(1);
        insumo.setNombre("Jeringa");

        Insumos insumosFalso = new Insumos();
        insumosFalso.setId(idSimulado);
        insumosFalso.setTratamiento(tratamiento);
        insumosFalso.setInsumo(insumo);

        InsumosDTO dtoEsperado = new InsumosDTO();
        dtoEsperado.setId(idSimulado);

        when(insumosRepository.findById(idSimulado)).thenReturn(Optional.of(insumosFalso));
        when(insumosValidaciones.convertirADTO(insumosFalso)).thenReturn(dtoEsperado);

        InsumosDTO resultado = insumosService.buscarPorId(idSimulado);

        assertNotNull(resultado);
        verify(insumosRepository, times(1)).findById(idSimulado);
    }

    @Test
    void testBuscarPorId_NoEncontrado() {
        Integer idInexistente = 999;
        when(insumosRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> insumosService.buscarPorId(idInexistente));
        verify(insumosRepository, times(1)).findById(idInexistente);
    }

    @Test
    void testGuardar_Exitoso() {
        Tratamiento tratamiento = new Tratamiento();
        tratamiento.setId(1);
        Insumo insumo = new Insumo();
        insumo.setId(1);

        Insumos insumosValido = new Insumos();
        insumosValido.setTratamiento(tratamiento);
        insumosValido.setInsumo(insumo);

        InsumosDTO dtoEsperado = new InsumosDTO();
        dtoEsperado.setId(1);

        when(insumosValidaciones.validarNullVacio(insumosValido)).thenReturn(true);
        when(insumosRepository.save(insumosValido)).thenReturn(insumosValido);
        when(insumosValidaciones.convertirADTO(insumosValido)).thenReturn(dtoEsperado);

        InsumosDTO resultado = insumosService.guardar(insumosValido);

        assertNotNull(resultado);
        verify(insumosRepository, times(1)).save(insumosValido);
    }

    @Test
    void testGuardar_ValidacionFallida() {
        Insumos insumosInvalido = new Insumos();

        when(insumosValidaciones.validarNullVacio(insumosInvalido)).thenReturn(false);

        InsumosDTO resultado = insumosService.guardar(insumosInvalido);

        assertNull(resultado);
        verify(insumosRepository, times(0)).save(insumosInvalido);
    }

    @Test
    void testEliminar_Exitoso() {
        Integer idSimulado = 5;
        Insumos insumosFalso = new Insumos();
        insumosFalso.setId(idSimulado);

        when(insumosRepository.findById(idSimulado)).thenReturn(Optional.of(insumosFalso));

        String resultado = insumosService.eliminar(idSimulado);

        assertNotNull(resultado);
        assertEquals(true, resultado.contains("eliminada"));
        verify(insumosRepository, times(1)).delete(insumosFalso);
    }
}