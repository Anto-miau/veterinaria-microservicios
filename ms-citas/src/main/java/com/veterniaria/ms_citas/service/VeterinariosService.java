package com.veterniaria.ms_citas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterniaria.ms_citas.DTO.VeterinariosDTO;
import com.veterniaria.ms_citas.model.Veterinarios;
import com.veterniaria.ms_citas.repository.VeterinariosRepository;

@Service
public class VeterinariosService {

    @Autowired
    private VeterinariosValidaciones veterinariosValidaciones;

    @Autowired
    private VeterinariosRepository veterinariosRepository;

    public List<VeterinariosDTO> obtenerTodos() {
        List<VeterinariosDTO> listaDTOs = new ArrayList<>();
        for (Veterinarios v : veterinariosRepository.findAll()) {
            listaDTOs.add(veterinariosValidaciones.convertirADTO(v));
        }
        return listaDTOs;
    }

    public VeterinariosDTO buscarPorId(Integer id) {
        Veterinarios veterinarios = veterinariosRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Registro no encontrado!"));
        return veterinariosValidaciones.convertirADTO(veterinarios);
    }

    public VeterinariosDTO guardar(Veterinarios nuevoVeterinarios) {
        if (veterinariosValidaciones.validarNullVacio(nuevoVeterinarios)) {
            Veterinarios guardado = veterinariosRepository.save(nuevoVeterinarios);
            return veterinariosValidaciones.convertirADTO(guardado);
        }
        return null;
    }

    public String eliminar(Integer id) {
        try {
            Veterinarios veterinarios = veterinariosRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! Registro con ID " + id + " no existe."));
            veterinariosRepository.delete(veterinarios);
            return "Relación cita-veterinario eliminada exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}
