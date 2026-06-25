package com.veterniaria.ms_mascotas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.veterniaria.ms_mascotas.DTO.RazaDTO;
import com.veterniaria.ms_mascotas.model.Raza;
import com.veterniaria.ms_mascotas.repository.RazaRepository;

public class RazaService {
    
    @Autowired
    private RazaValidaciones razaValidaciones;

    @Autowired
    private RazaRepository razaRepository;

    public List<RazaDTO> obtenerTodos() {
        List<RazaDTO> listaDTOs = new ArrayList<>();
        for (Raza r : razaRepository.findAll()) {
            listaDTOs.add(razaValidaciones.convertirADTO(r));
        }
        return listaDTOs;
    }

    public RazaDTO buscarPorId(Integer id) {
        Raza raza = razaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Raza no encontrada!"));
        return razaValidaciones.convertirADTO(raza);
    }

    public RazaDTO guardar(Raza nuevaRaza) {
        if (razaValidaciones.validarNullVacio(nuevaRaza)) {
            Raza guardada = razaRepository.save(nuevaRaza);
            return razaValidaciones.convertirADTO(guardada);
        }
        return null;
    }

    public RazaDTO actualizar(Integer id, Raza razaActualizada) {
        Raza raza = razaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Raza no existe en los registros!"));

        if (razaActualizada.getNombre() != null) {
            raza.setNombre(razaActualizada.getNombre());
        }
        if (razaActualizada.getEspecie() != null) {
            raza.setEspecie(razaActualizada.getEspecie());
        }

        return razaValidaciones.convertirADTO(razaRepository.save(raza));
    }

    public String eliminar(Integer id) {
        try {
            Raza raza = razaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! Raza con ID " + id + " no existe."));
            razaRepository.delete(raza);
            return "Raza '" + raza.getNombre() + "' ha sido eliminada exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}
