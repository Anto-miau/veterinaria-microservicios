package com.veterniaria.ms_citas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterniaria.ms_citas.DTO.TratamientosDTO;
import com.veterniaria.ms_citas.model.Tratamientos;
import com.veterniaria.ms_citas.repository.TratamientosRepository;

@Service
public class TratamientosService {

    @Autowired
    private TratamientosValidaciones tratamientosValidaciones;

    @Autowired
    private TratamientosRepository tratamientosRepository;

    public List<TratamientosDTO> obtenerTodos() {
        List<TratamientosDTO> listaDTOs = new ArrayList<>();
        for (Tratamientos t : tratamientosRepository.findAll()) {
            listaDTOs.add(tratamientosValidaciones.convertirADTO(t));
        }
        return listaDTOs;
    }

    public TratamientosDTO buscarPorId(Integer id) {
        Tratamientos tratamientos = tratamientosRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Registro no encontrado!"));
        return tratamientosValidaciones.convertirADTO(tratamientos);
    }

    public TratamientosDTO guardar(Tratamientos nuevoTratamientos) {
        if (tratamientosValidaciones.validarNullVacio(nuevoTratamientos)) {
            Tratamientos guardado = tratamientosRepository.save(nuevoTratamientos);
            return tratamientosValidaciones.convertirADTO(guardado);
        }
        return null;
    }

    public String eliminar(Integer id) {
        try {
            Tratamientos tratamientos = tratamientosRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! Registro con ID " + id + " no existe."));
            tratamientosRepository.delete(tratamientos);
            return "Relación cita-tratamiento eliminada exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}
