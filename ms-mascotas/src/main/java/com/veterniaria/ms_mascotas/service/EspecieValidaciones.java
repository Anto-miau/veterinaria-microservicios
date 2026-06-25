package com.veterniaria.ms_mascotas.service;

import org.springframework.stereotype.Service;

import com.veterniaria.ms_mascotas.DTO.EspecieDTO;
import com.veterniaria.ms_mascotas.model.Especie;


@Service
public class EspecieValidaciones {
    
    public Boolean validarNullVacio(Especie especie) {
        if (especie.getNombre() == null || especie.getNombre().trim().length() == 0) {
            return false;
        }
        return true;
    }

    public EspecieDTO convertirADTO(Especie especie) {
        EspecieDTO dto = new EspecieDTO();
        dto.setId(especie.getId());
        dto.setNombre(especie.getNombre());
        return dto;
    }
}
