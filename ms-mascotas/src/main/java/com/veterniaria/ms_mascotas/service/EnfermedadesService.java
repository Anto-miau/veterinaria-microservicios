package com.veterniaria.ms_mascotas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterniaria.ms_mascotas.DTO.EnfermedadesDTO;
import com.veterniaria.ms_mascotas.model.Enfermedades;
import com.veterniaria.ms_mascotas.repository.EnfermedadesRepository;

@Service
public class EnfermedadesService {

    @Autowired
    private EnfermedadesValidaciones enfermedadesValidaciones;

    @Autowired
    private EnfermedadesRepository enfermedadesRepository;

    public List<EnfermedadesDTO> obtenerTodos() {
        List<EnfermedadesDTO> listaDTOs = new ArrayList<>();
        for (Enfermedades e : enfermedadesRepository.findAll()) {
            listaDTOs.add(enfermedadesValidaciones.convertirADTO(e));
        }
        return listaDTOs;
    }

    public EnfermedadesDTO buscarPorId(Integer id) {
        Enfermedades enfermedades = enfermedadesRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Registro no encontrado!"));
        return enfermedadesValidaciones.convertirADTO(enfermedades);
    }

    public EnfermedadesDTO guardar(Enfermedades nuevoEnfermedades) {
        if (enfermedadesValidaciones.validarNullVacio(nuevoEnfermedades)) {
            Enfermedades guardado = enfermedadesRepository.save(nuevoEnfermedades);
            return enfermedadesValidaciones.convertirADTO(guardado);
        }
        return null;
    }

    public String eliminar(Integer id) {
        try {
            Enfermedades enfermedades = enfermedadesRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! Registro con ID " + id + " no existe."));
            enfermedadesRepository.delete(enfermedades);
            return "Relación mascota-enfermedad eliminada exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}
