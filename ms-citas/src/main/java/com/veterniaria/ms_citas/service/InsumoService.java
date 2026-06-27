package com.veterniaria.ms_citas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterniaria.ms_citas.DTO.InsumoDTO;
import com.veterniaria.ms_citas.model.Insumo;
import com.veterniaria.ms_citas.repository.InsumoRepository;

@Service
public class InsumoService {

    @Autowired
    private InsumoValidaciones insumoValidaciones;

    @Autowired
    private InsumoRepository insumoRepository;

    public List<InsumoDTO> obtenerTodos() {
        List<InsumoDTO> listaDTOs = new ArrayList<>();
        for (Insumo i : insumoRepository.findAll()) {
            listaDTOs.add(insumoValidaciones.convertirADTO(i));
        }
        return listaDTOs;
    }

    public InsumoDTO buscarPorId(Integer id) {
        Insumo insumo = insumoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Insumo no encontrado!"));
        return insumoValidaciones.convertirADTO(insumo);
    }

    public InsumoDTO guardar(Insumo nuevoInsumo) {
        if (insumoValidaciones.validarNullVacio(nuevoInsumo)) {
            Insumo guardado = insumoRepository.save(nuevoInsumo);
            return insumoValidaciones.convertirADTO(guardado);
        }
        return null;
    }

    public InsumoDTO actualizar(Integer id, Insumo insumoActualizado) {
        Insumo insumo = insumoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Insumo no existe en los registros!"));

        if (insumoActualizado.getNombre() != null) {
            insumo.setNombre(insumoActualizado.getNombre());
        }
        if (insumoActualizado.getDescripcion() != null) {
            insumo.setDescripcion(insumoActualizado.getDescripcion());
        }
        if (insumoActualizado.getStock() != null) {
            insumo.setStock(insumoActualizado.getStock());
        }

        return insumoValidaciones.convertirADTO(insumoRepository.save(insumo));
    }

    public String eliminar(Integer id) {
        try {
            Insumo insumo = insumoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! Insumo con ID " + id + " no existe."));
            insumoRepository.delete(insumo);
            return "Insumo '" + insumo.getNombre() + "' ha sido eliminado exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}
