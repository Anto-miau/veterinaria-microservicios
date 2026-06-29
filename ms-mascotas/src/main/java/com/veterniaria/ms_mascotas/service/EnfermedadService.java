package com.veterniaria.ms_mascotas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterniaria.ms_mascotas.DTO.EnfermedadDTO;
import com.veterniaria.ms_mascotas.model.Enfermedad;
import com.veterniaria.ms_mascotas.repository.EnfermedadRepository;

@Service
public class EnfermedadService {

    @Autowired
    private EnfermedadValidaciones enfermedadValidaciones;

    @Autowired
    private EnfermedadRepository enfermedadRepository;

    public List<EnfermedadDTO> obtenerTodos() {
        List<EnfermedadDTO> listaDTOs = new ArrayList<>();
        for (Enfermedad e : enfermedadRepository.findAll()) {
            listaDTOs.add(enfermedadValidaciones.convertirADTO(e));
        }
        return listaDTOs;
    }

    public EnfermedadDTO buscarPorId(Integer id) {
        Enfermedad enfermedad = enfermedadRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Enfermedad no encontrada!"));
        return enfermedadValidaciones.convertirADTO(enfermedad);
    }

    public EnfermedadDTO guardar(Enfermedad nuevaEnfermedad) {
        if (enfermedadValidaciones.validarNullVacio(nuevaEnfermedad)) {
            Enfermedad guardada = enfermedadRepository.save(nuevaEnfermedad);
            return enfermedadValidaciones.convertirADTO(guardada);
        }
        return null;
    }

    public EnfermedadDTO actualizar(Integer id, Enfermedad enfermedadActualizada) {
        Enfermedad enfermedad = enfermedadRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Enfermedad no existe en los registros!"));

        if (enfermedadActualizada.getNombre() != null) {
            enfermedad.setNombre(enfermedadActualizada.getNombre());
        }
        if (enfermedadActualizada.getDescripcion() != null) {
            enfermedad.setDescripcion(enfermedadActualizada.getDescripcion());
        }

        return enfermedadValidaciones.convertirADTO(enfermedadRepository.save(enfermedad));
    }

    public String eliminar(Integer id) {
        try {
            Enfermedad enfermedad = enfermedadRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! Enfermedad con ID " + id + " no existe."));
            enfermedadRepository.delete(enfermedad);
            return "Enfermedad '" + enfermedad.getNombre() + "' ha sido eliminada exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}
