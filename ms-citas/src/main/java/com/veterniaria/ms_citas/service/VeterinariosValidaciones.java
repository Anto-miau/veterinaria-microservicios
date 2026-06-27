package com.veterniaria.ms_citas.service;

import org.springframework.stereotype.Service;

import com.veterniaria.ms_citas.DTO.VeterinariosDTO;
import com.veterniaria.ms_citas.model.Veterinarios;

@Service
public class VeterinariosValidaciones {

    public Boolean validarNullVacio(Veterinarios veterinarios) {
        if (veterinarios.getCita() == null) {
            return false;
        }
        if (veterinarios.getVeterinarioId() == null) {
            return false;
        }
        return true;
    }

    public VeterinariosDTO convertirADTO(Veterinarios veterinarios) {
        VeterinariosDTO dto = new VeterinariosDTO();
        dto.setId(veterinarios.getId());

        if (veterinarios.getCita() != null) {
            dto.setCitaId(veterinarios.getCita().getId());
            dto.setCitaMotivo(veterinarios.getCita().getMotivo());
        }
        dto.setVeterinarioId(veterinarios.getVeterinarioId());
        return dto;
    }
}
