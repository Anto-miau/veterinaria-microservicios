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

import com.veterniaria.ms_citas.DTO.VeterinariosDTO;
import com.veterniaria.ms_citas.model.Cita;
import com.veterniaria.ms_citas.model.Veterinarios;
import com.veterniaria.ms_citas.repository.VeterinariosRepository;

@ExtendWith(MockitoExtension.class)
class VeterinariosServiceTest {

    @Mock
    private VeterinariosRepository veterinariosRepository;

    @Mock
    private VeterinariosValidaciones veterinariosValidaciones;

    @InjectMocks
    private VeterinariosService veterinariosService;

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

        Veterinarios veterinariosFalso = new Veterinarios();
        veterinariosFalso.setId(idSimulado);
        veterinariosFalso.setCita(cita);
        veterinariosFalso.setVeterinarioId(1);

        VeterinariosDTO dtoEsperado = new VeterinariosDTO();
        dtoEsperado.setId(idSimulado);

        when(veterinariosRepository.findById(idSimulado)).thenReturn(Optional.of(veterinariosFalso));
        when(veterinariosValidaciones.convertirADTO(veterinariosFalso)).thenReturn(dtoEsperado);

        VeterinariosDTO resultado = veterinariosService.buscarPorId(idSimulado);

        assertNotNull(resultado);
        verify(veterinariosRepository, times(1)).findById(idSimulado);
    }

    @Test
    void testBuscarPorId_NoEncontrado() {
        Integer idInexistente = 999;
        when(veterinariosRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> veterinariosService.buscarPorId(idInexistente));
        verify(veterinariosRepository, times(1)).findById(idInexistente);
    }

    @Test
    void testGuardar_Exitoso() {
        Cita cita = new Cita();
        cita.setId(1);

        Veterinarios veterinariosValido = new Veterinarios();
        veterinariosValido.setCita(cita);
        veterinariosValido.setVeterinarioId(1);

        VeterinariosDTO dtoEsperado = new VeterinariosDTO();
        dtoEsperado.setId(1);

        when(veterinariosValidaciones.validarNullVacio(veterinariosValido)).thenReturn(true);
        when(veterinariosRepository.save(veterinariosValido)).thenReturn(veterinariosValido);
        when(veterinariosValidaciones.convertirADTO(veterinariosValido)).thenReturn(dtoEsperado);

        VeterinariosDTO resultado = veterinariosService.guardar(veterinariosValido);

        assertNotNull(resultado);
        verify(veterinariosRepository, times(1)).save(veterinariosValido);
    }

    @Test
    void testGuardar_ValidacionFallida() {
        Veterinarios veterinariosInvalido = new Veterinarios();

        when(veterinariosValidaciones.validarNullVacio(veterinariosInvalido)).thenReturn(false);

        VeterinariosDTO resultado = veterinariosService.guardar(veterinariosInvalido);

        assertNull(resultado);
        verify(veterinariosRepository, times(0)).save(veterinariosInvalido);
    }

    @Test
    void testEliminar_Exitoso() {
        Integer idSimulado = 5;
        Veterinarios veterinariosFalso = new Veterinarios();
        veterinariosFalso.setId(idSimulado);

        when(veterinariosRepository.findById(idSimulado)).thenReturn(Optional.of(veterinariosFalso));

        String resultado = veterinariosService.eliminar(idSimulado);

        assertNotNull(resultado);
        assertEquals(true, resultado.contains("eliminada"));
        verify(veterinariosRepository, times(1)).delete(veterinariosFalso);
    }
}