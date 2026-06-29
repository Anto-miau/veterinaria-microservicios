package com.veterniaria.ms_mascotas.service;

import org.springframework.stereotype.Service;

import com.veterniaria.ms_mascotas.DTO.EnfermedadDTO;
import com.veterniaria.ms_mascotas.model.Enfermedad;

@Service
public class EnfermedadValidaciones {

    public Boolean validarNullVacio(Enfermedad enfermedad) {
        if (enfermedad.getNombre() == null || enfermedad.getNombre().trim().length() == 0) {
            return false;
        }
        if (enfermedad.getDescripcion() == null || enfermedad.getDescripcion().trim().length() == 0) {
            return false;
        }
        return true;
    }

    public EnfermedadDTO convertirADTO(Enfermedad enfermedad) {
        EnfermedadDTO dto = new EnfermedadDTO();
        dto.setId(enfermedad.getId());
        dto.setNombre(enfermedad.getNombre());
        dto.setDescripcion(enfermedad.getDescripcion());
        return dto;
    }
}
