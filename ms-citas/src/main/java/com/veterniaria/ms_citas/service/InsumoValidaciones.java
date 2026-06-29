package com.veterniaria.ms_citas.service;

import org.springframework.stereotype.Service;

import com.veterniaria.ms_citas.DTO.InsumoDTO;
import com.veterniaria.ms_citas.model.Insumo;

@Service
public class InsumoValidaciones {

    public Boolean validarNullVacio(Insumo insumo) {
        if (insumo.getNombre() == null || insumo.getNombre().trim().length() == 0) {
            return false;
        }
        if (insumo.getDescripcion() == null || insumo.getDescripcion().trim().length() == 0) {
            return false;
        }
        if (insumo.getStock() == null || insumo.getStock() < 0) {
            return false;
        }
        return true;
    }

    public InsumoDTO convertirADTO(Insumo insumo) {
        InsumoDTO dto = new InsumoDTO();
        dto.setId(insumo.getId());
        dto.setNombre(insumo.getNombre());
        dto.setDescripcion(insumo.getDescripcion());
        dto.setStock(insumo.getStock());
        return dto;
    }
}
