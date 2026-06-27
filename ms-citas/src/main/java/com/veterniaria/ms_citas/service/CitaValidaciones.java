package com.veterniaria.ms_citas.service;

import org.springframework.stereotype.Service;

import com.veterniaria.ms_citas.DTO.CitaDTO;
import com.veterniaria.ms_citas.model.Cita;

@Service
public class CitaValidaciones {

    public Boolean validarNullVacio(Cita cita) {
        if (cita.getMotivo() == null || cita.getMotivo().trim().length() == 0) {
            return false;
        }
        if (cita.getFechaHora() == null) {
            return false;
        }
        if (cita.getEstado() == null || cita.getEstado().trim().length() == 0) {
            return false;
        }
        if (cita.getMascotaId() == null) {
            return false;
        }
        return true;
    }

    public CitaDTO convertirADTO(Cita cita) {
        CitaDTO dto = new CitaDTO();
        dto.setId(cita.getId());
        dto.setMotivo(cita.getMotivo());
        dto.setFechaHora(cita.getFechaHora());
        dto.setEstado(cita.getEstado());
        dto.setMascotaId(cita.getMascotaId());
        return dto;
    }
}
