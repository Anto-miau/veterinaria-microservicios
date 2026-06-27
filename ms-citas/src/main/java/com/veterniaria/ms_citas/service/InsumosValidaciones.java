package com.veterniaria.ms_citas.service;

import org.springframework.stereotype.Service;

import com.veterniaria.ms_citas.DTO.InsumosDTO;
import com.veterniaria.ms_citas.model.Insumos;

@Service
public class InsumosValidaciones {

    public Boolean validarNullVacio(Insumos insumos) {
        if (insumos.getTratamiento() == null) {
            return false;
        }
        if (insumos.getInsumo() == null) {
            return false;
        }
        return true;
    }

    public InsumosDTO convertirADTO(Insumos insumos) {
        InsumosDTO dto = new InsumosDTO();
        dto.setId(insumos.getId());

        if (insumos.getTratamiento() != null) {
            dto.setTratamientoId(insumos.getTratamiento().getId());
            dto.setTratamientoDiagnostico(insumos.getTratamiento().getDiagnostico());
        }
        if (insumos.getInsumo() != null) {
            dto.setInsumoId(insumos.getInsumo().getId());
            dto.setInsumoNombre(insumos.getInsumo().getNombre());
        }
        return dto;
    }
}
