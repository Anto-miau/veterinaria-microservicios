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

import com.veterniaria.ms_mascotas.DTO.MascotaDTO;
import com.veterniaria.ms_mascotas.model.Mascota;
import com.veterniaria.ms_mascotas.repository.MascotaRepository;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
public class MascotaServiceTest {

    @Mock
    private MascotaRepository mascotaRepository;

    @Mock
    private MascotaValidaciones mascotaValidaciones;

    @InjectMocks
    private MascotaService mascotaService;

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscarPorId_Exitoso() {
        // GIVEN: una mascota simulada existe en la base de datos
        Integer idSimulado = 1;
        String nombreAleatorio = faker.dog().name();
        Mascota mascotaFalsa = new Mascota();
        mascotaFalsa.setId(idSimulado);
        mascotaFalsa.setNombre(nombreAleatorio);

        MascotaDTO dtoEsperado = new MascotaDTO();
        dtoEsperado.setId(idSimulado);
        dtoEsperado.setNombre(nombreAleatorio);

        when(mascotaRepository.findById(idSimulado)).thenReturn(Optional.of(mascotaFalsa));
        when(mascotaValidaciones.convertirADTO(mascotaFalsa)).thenReturn(dtoEsperado);

        // WHEN: buscamos esa mascota por id
        MascotaDTO resultado = mascotaService.buscarPorId(idSimulado);

        // THEN: el DTO debe contener los datos esperados
        assertNotNull(resultado, "El DTO resultante no debería ser nulo");
        assertEquals(nombreAleatorio, resultado.getNombre(), "El nombre debe coincidir con el de la base de datos");
        verify(mascotaRepository, times(1)).findById(idSimulado);
    }

    @Test
    void testBuscarPorId_NoEncontrado() {
        // GIVEN: un id que no existe en la base de datos
        Integer idInexistente = 999;
        when(mascotaRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // WHEN / THEN: debe lanzar una excepción
        assertThrows(RuntimeException.class, () -> mascotaService.buscarPorId(idInexistente));
        verify(mascotaRepository, times(1)).findById(idInexistente);
    }

    @Test
    void testGuardar_Exitoso() {
        // GIVEN: una mascota válida
        Mascota mascotaValida = new Mascota();
        mascotaValida.setNombre(faker.dog().name());
        mascotaValida.setColor(faker.color().name());
        mascotaValida.setEdad(faker.number().numberBetween(1, 15));
        mascotaValida.setSexo("Hembra");
        mascotaValida.setDuenoId((long) faker.number().numberBetween(1, 100));

        MascotaDTO dtoEsperado = new MascotaDTO();
        dtoEsperado.setNombre(mascotaValida.getNombre());

        when(mascotaValidaciones.validarNullVacio(mascotaValida)).thenReturn(true);
        when(mascotaRepository.save(mascotaValida)).thenReturn(mascotaValida);
        when(mascotaValidaciones.convertirADTO(mascotaValida)).thenReturn(dtoEsperado);

        // WHEN: guardamos la mascota
        MascotaDTO resultado = mascotaService.guardar(mascotaValida);

        // THEN: el DTO debe haberse creado correctamente
        assertNotNull(resultado);
        assertEquals(mascotaValida.getNombre(), resultado.getNombre());
        verify(mascotaRepository, times(1)).save(mascotaValida);
    }

    @Test
    void testGuardar_ValidacionFallida() {
        // GIVEN: una mascota sin nombre (inválida)
        Mascota mascotaInvalida = new Mascota();
        mascotaInvalida.setNombre("");
        mascotaInvalida.setColor(faker.color().name());
        mascotaInvalida.setSexo("Macho");
        mascotaInvalida.setDuenoId(1L);

        when(mascotaValidaciones.validarNullVacio(mascotaInvalida)).thenReturn(false);

        // WHEN: intentamos guardar
        MascotaDTO resultado = mascotaService.guardar(mascotaInvalida);

        // THEN: no debe guardarse, debe retornar null
        assertNull(resultado, "No debería guardar una mascota sin nombre");
        verify(mascotaRepository, times(0)).save(mascotaInvalida);
    }

    @Test
    void testEliminar_Exitoso() {
        // GIVEN: una mascota existente
        Integer idSimulado = 5;
        Mascota mascotaFalsa = new Mascota();
        mascotaFalsa.setId(idSimulado);
        mascotaFalsa.setNombre(faker.dog().name());

        when(mascotaRepository.findById(idSimulado)).thenReturn(Optional.of(mascotaFalsa));

        // WHEN: eliminamos la mascota
        String resultado = mascotaService.eliminar(idSimulado);

        // THEN: debe confirmar la eliminación
        assertNotNull(resultado);
        assertEquals(true, resultado.contains("exitosamente"));
        verify(mascotaRepository, times(1)).delete(mascotaFalsa);
    }
}
