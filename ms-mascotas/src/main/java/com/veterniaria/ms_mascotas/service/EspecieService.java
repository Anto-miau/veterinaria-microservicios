package com.veterniaria.ms_mascotas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterniaria.ms_mascotas.DTO.EspecieDTO;
import com.veterniaria.ms_mascotas.model.Especie;
import com.veterniaria.ms_mascotas.repository.EspecieRepository;

@Service
public class EspecieService {

    @Autowired
    private EspecieValidaciones especieValidaciones;

    @Autowired
    private EspecieRepository especieRepository;

    public List<EspecieDTO> obtenerTodos() {
        List<EspecieDTO> listaDTOs = new ArrayList<>();
        for (Especie e : especieRepository.findAll()) {
            listaDTOs.add(especieValidaciones.convertirADTO(e));
        }
        return listaDTOs;
    }

    public EspecieDTO buscarPorId(Integer id) {
        Especie especie = especieRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Especie no encontrada!"));
        return especieValidaciones.convertirADTO(especie);
    }

    public EspecieDTO guardar(Especie nuevaEspecie) {
        if (especieValidaciones.validarNullVacio(nuevaEspecie)) {
            Especie guardada = especieRepository.save(nuevaEspecie);
            return especieValidaciones.convertirADTO(guardada);
        }
        return null;
    }

    public EspecieDTO actualizar(Integer id, Especie especieActualizada) {
        Especie especie = especieRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Especie no existe en los registros!"));

        if (especieActualizada.getNombre() != null) {
            especie.setNombre(especieActualizada.getNombre());
        }

        return especieValidaciones.convertirADTO(especieRepository.save(especie));
    }

    public String eliminar(Integer id) {
        try {
            Especie especie = especieRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! Especie con ID " + id + " no existe."));
            especieRepository.delete(especie);
            return "Especie '" + especie.getNombre() + "' ha sido eliminada exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}
