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

import com.veterniaria.ms_mascotas.DTO.ContactosDTO;
import com.veterniaria.ms_mascotas.model.Contacto;
import com.veterniaria.ms_mascotas.model.Contactos;
import com.veterniaria.ms_mascotas.model.Mascota;
import com.veterniaria.ms_mascotas.repository.ContactosRepository;

@ExtendWith(MockitoExtension.class)
class ContactosServiceTest {

    @Mock
    private ContactosRepository contactosRepository;

    @Mock
    private ContactosValidaciones contactosValidaciones;

    @InjectMocks
    private ContactosService contactosService;

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
        Contacto contacto = new Contacto();
        contacto.setId(1);
        contacto.setNombre("Juan Pérez");

        Contactos contactosFalso = new Contactos();
        contactosFalso.setId(idSimulado);
        contactosFalso.setMascota(mascota);
        contactosFalso.setContacto(contacto);

        ContactosDTO dtoEsperado = new ContactosDTO();
        dtoEsperado.setId(idSimulado);

        when(contactosRepository.findById(idSimulado)).thenReturn(Optional.of(contactosFalso));
        when(contactosValidaciones.convertirADTO(contactosFalso)).thenReturn(dtoEsperado);

        ContactosDTO resultado = contactosService.buscarPorId(idSimulado);

        assertNotNull(resultado);
        verify(contactosRepository, times(1)).findById(idSimulado);
    }

    @Test
    void testBuscarPorId_NoEncontrado() {
        Integer idInexistente = 999;
        when(contactosRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> contactosService.buscarPorId(idInexistente));
        verify(contactosRepository, times(1)).findById(idInexistente);
    }

    @Test
    void testGuardar_Exitoso() {
        Mascota mascota = new Mascota();
        mascota.setId(1);
        Contacto contacto = new Contacto();
        contacto.setId(1);

        Contactos contactosValido = new Contactos();
        contactosValido.setMascota(mascota);
        contactosValido.setContacto(contacto);

        ContactosDTO dtoEsperado = new ContactosDTO();
        dtoEsperado.setId(1);

        when(contactosValidaciones.validarNullVacio(contactosValido)).thenReturn(true);
        when(contactosRepository.save(contactosValido)).thenReturn(contactosValido);
        when(contactosValidaciones.convertirADTO(contactosValido)).thenReturn(dtoEsperado);

        ContactosDTO resultado = contactosService.guardar(contactosValido);

        assertNotNull(resultado);
        verify(contactosRepository, times(1)).save(contactosValido);
    }

    @Test
    void testGuardar_ValidacionFallida() {
        Contactos contactosInvalido = new Contactos();
        // sin mascota ni contacto asignados

        when(contactosValidaciones.validarNullVacio(contactosInvalido)).thenReturn(false);

        ContactosDTO resultado = contactosService.guardar(contactosInvalido);

        assertNull(resultado);
        verify(contactosRepository, times(0)).save(contactosInvalido);
    }

    @Test
    void testEliminar_Exitoso() {
        Integer idSimulado = 5;
        Contactos contactosFalso = new Contactos();
        contactosFalso.setId(idSimulado);

        when(contactosRepository.findById(idSimulado)).thenReturn(Optional.of(contactosFalso));

        String resultado = contactosService.eliminar(idSimulado);

        assertNotNull(resultado);
        assertEquals(true, resultado.contains("eliminada"));
        verify(contactosRepository, times(1)).delete(contactosFalso);
    }
}