package com.veterniaria.ms_mascotas.service;

import org.springframework.stereotype.Service;

import com.veterniaria.ms_mascotas.DTO.EnfermedadesDTO;
import com.veterniaria.ms_mascotas.model.Enfermedades;

@Service
public class EnfermedadesValidaciones {

    public Boolean validarNullVacio(Enfermedades enfermedades) {
        if (enfermedades.getMascota() == null) {
            return false;
        }
        if (enfermedades.getEnfermedad() == null) {
            return false;
        }
        return true;
    }

    public EnfermedadesDTO convertirADTO(Enfermedades enfermedades) {
        EnfermedadesDTO dto = new EnfermedadesDTO();
        dto.setId(enfermedades.getId());

        if (enfermedades.getMascota() != null) {
            dto.setMascotaId(enfermedades.getMascota().getId());
            dto.setMascotaNombre(enfermedades.getMascota().getNombre());
        }
        if (enfermedades.getEnfermedad() != null) {
            dto.setEnfermedadId(enfermedades.getEnfermedad().getId());
            dto.setEnfermedadNombre(enfermedades.getEnfermedad().getNombre());
        }
        return dto;
    }
}
