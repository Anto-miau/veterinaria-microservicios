package com.veterniaria.ms_citas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterniaria.ms_citas.DTO.InsumosDTO;
import com.veterniaria.ms_citas.model.Insumos;
import com.veterniaria.ms_citas.repository.InsumosRepository;

@Service
public class InsumosService {

    @Autowired
    private InsumosValidaciones insumosValidaciones;

    @Autowired
    private InsumosRepository insumosRepository;

    public List<InsumosDTO> obtenerTodos() {
        List<InsumosDTO> listaDTOs = new ArrayList<>();
        for (Insumos i : insumosRepository.findAll()) {
            listaDTOs.add(insumosValidaciones.convertirADTO(i));
        }
        return listaDTOs;
    }

    public InsumosDTO buscarPorId(Integer id) {
        Insumos insumos = insumosRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Registro no encontrado!"));
        return insumosValidaciones.convertirADTO(insumos);
    }

    public InsumosDTO guardar(Insumos nuevoInsumos) {
        if (insumosValidaciones.validarNullVacio(nuevoInsumos)) {
            Insumos guardado = insumosRepository.save(nuevoInsumos);
            return insumosValidaciones.convertirADTO(guardado);
        }
        return null;
    }

    public String eliminar(Integer id) {
        try {
            Insumos insumos = insumosRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! Registro con ID " + id + " no existe."));
            insumosRepository.delete(insumos);
            return "Relación tratamiento-insumo eliminada exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}
