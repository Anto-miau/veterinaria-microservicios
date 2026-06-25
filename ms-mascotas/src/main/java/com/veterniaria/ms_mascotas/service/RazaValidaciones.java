package com.veterniaria.ms_mascotas.service;

import com.veterniaria.ms_mascotas.DTO.RazaDTO;
import com.veterniaria.ms_mascotas.model.Raza;

public class RazaValidaciones {
    
    public Boolean validarNullVacio(Raza raza) {
        if (raza.getNombre() == null || raza.getNombre().trim().length() == 0) {
            return false;
        }
        if (raza.getEspecie() == null) {
            return false;
        }
        return true;
    }

    public RazaDTO convertirADTO(Raza raza) {
        RazaDTO dto = new RazaDTO();
        dto.setId(raza.getId());
        dto.setNombre(raza.getNombre());

        if (raza.getEspecie() != null) {
            dto.setEspecieId(raza.getEspecie().getId());
            dto.setEspecieNombre(raza.getEspecie().getNombre());
        }
        return dto;
    }
}
