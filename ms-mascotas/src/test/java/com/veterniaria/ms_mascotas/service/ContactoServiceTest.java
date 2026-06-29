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

import com.veterniaria.ms_mascotas.DTO.ContactoDTO;
import com.veterniaria.ms_mascotas.model.Contacto;
import com.veterniaria.ms_mascotas.repository.ContactoRepository;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class ContactoServiceTest {

    @Mock
    private ContactoRepository contactoRepository;

    @Mock
    private ContactoValidaciones contactoValidaciones;

    @InjectMocks
    private ContactoService contactoService;

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscarPorId_Exitoso() {
        Integer idSimulado = 1;
        String nombreAleatorio = faker.name().fullName();
        Contacto contactoFalso = new Contacto();
        contactoFalso.setId(idSimulado);
        contactoFalso.setNombre(nombreAleatorio);
        contactoFalso.setTelefono(faker.phoneNumber().phoneNumber());
        contactoFalso.setCorreo(faker.internet().emailAddress());

        ContactoDTO dtoEsperado = new ContactoDTO();
        dtoEsperado.setId(idSimulado);
        dtoEsperado.setNombre(nombreAleatorio);

        when(contactoRepository.findById(idSimulado)).thenReturn(Optional.of(contactoFalso));
        when(contactoValidaciones.convertirADTO(contactoFalso)).thenReturn(dtoEsperado);

        ContactoDTO resultado = contactoService.buscarPorId(idSimulado);

        assertNotNull(resultado);
        assertEquals(nombreAleatorio, resultado.getNombre());
        verify(contactoRepository, times(1)).findById(idSimulado);
    }

    @Test
    void testBuscarPorId_NoEncontrado() {
        Integer idInexistente = 999;
        when(contactoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> contactoService.buscarPorId(idInexistente));
        verify(contactoRepository, times(1)).findById(idInexistente);
    }

    @Test
    void testGuardar_Exitoso() {
        Contacto contactoValido = new Contacto();
        contactoValido.setNombre(faker.name().fullName());
        contactoValido.setTelefono(faker.phoneNumber().phoneNumber());
        contactoValido.setCorreo(faker.internet().emailAddress());

        ContactoDTO dtoEsperado = new ContactoDTO();
        dtoEsperado.setNombre(contactoValido.getNombre());

        when(contactoValidaciones.validarNullVacio(contactoValido)).thenReturn(true);
        when(contactoRepository.save(contactoValido)).thenReturn(contactoValido);
        when(contactoValidaciones.convertirADTO(contactoValido)).thenReturn(dtoEsperado);

        ContactoDTO resultado = contactoService.guardar(contactoValido);

        assertNotNull(resultado);
        assertEquals(contactoValido.getNombre(), resultado.getNombre());
        verify(contactoRepository, times(1)).save(contactoValido);
    }

    @Test
    void testGuardar_ValidacionFallida() {
        Contacto contactoInvalido = new Contacto();
        contactoInvalido.setNombre("");

        when(contactoValidaciones.validarNullVacio(contactoInvalido)).thenReturn(false);

        ContactoDTO resultado = contactoService.guardar(contactoInvalido);

        assertNull(resultado);
        verify(contactoRepository, times(0)).save(contactoInvalido);
    }

    @Test
    void testEliminar_Exitoso() {
        Integer idSimulado = 5;
        Contacto contactoFalso = new Contacto();
        contactoFalso.setId(idSimulado);
        contactoFalso.setNombre(faker.name().fullName());

        when(contactoRepository.findById(idSimulado)).thenReturn(Optional.of(contactoFalso));

        String resultado = contactoService.eliminar(idSimulado);

        assertNotNull(resultado);
        assertEquals(true, resultado.contains("exitosamente"));
        verify(contactoRepository, times(1)).delete(contactoFalso);
    }
}