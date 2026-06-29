package com.veterniaria.ms_citas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterniaria.ms_citas.DTO.TratamientoDTO;
import com.veterniaria.ms_citas.model.Tratamiento;
import com.veterniaria.ms_citas.repository.TratamientoRepository;

@Service
public class TratamientoService {

    @Autowired
    private TratamientoValidaciones tratamientoValidaciones;

    @Autowired
    private TratamientoRepository tratamientoRepository;

    public List<TratamientoDTO> obtenerTodos() {
        List<TratamientoDTO> listaDTOs = new ArrayList<>();
        for (Tratamiento t : tratamientoRepository.findAll()) {
            listaDTOs.add(tratamientoValidaciones.convertirADTO(t));
        }
        return listaDTOs;
    }

    public TratamientoDTO buscarPorId(Integer id) {
        Tratamiento tratamiento = tratamientoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Tratamiento no encontrado!"));
        return tratamientoValidaciones.convertirADTO(tratamiento);
    }

    public TratamientoDTO guardar(Tratamiento nuevoTratamiento) {
        if (tratamientoValidaciones.validarNullVacio(nuevoTratamiento)) {
            Tratamiento guardado = tratamientoRepository.save(nuevoTratamiento);
            return tratamientoValidaciones.convertirADTO(guardado);
        }
        return null;
    }

    public TratamientoDTO actualizar(Integer id, Tratamiento tratamientoActualizado) {
        Tratamiento tratamiento = tratamientoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Tratamiento no existe en los registros!"));

        if (tratamientoActualizado.getDiagnostico() != null) {
            tratamiento.setDiagnostico(tratamientoActualizado.getDiagnostico());
        }
        if (tratamientoActualizado.getDescripcion() != null) {
            tratamiento.setDescripcion(tratamientoActualizado.getDescripcion());
        }
        if (tratamientoActualizado.getFechaInicio() != null) {
            tratamiento.setFechaInicio(tratamientoActualizado.getFechaInicio());
        }
        if (tratamientoActualizado.getFechaTermino() != null) {
            tratamiento.setFechaTermino(tratamientoActualizado.getFechaTermino());
        }

        return tratamientoValidaciones.convertirADTO(tratamientoRepository.save(tratamiento));
    }

    public String eliminar(Integer id) {
        try {
            Tratamiento tratamiento = tratamientoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! Tratamiento con ID " + id + " no existe."));
            tratamientoRepository.delete(tratamiento);
            return "Tratamiento '" + tratamiento.getDiagnostico() + "' ha sido eliminado exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}
