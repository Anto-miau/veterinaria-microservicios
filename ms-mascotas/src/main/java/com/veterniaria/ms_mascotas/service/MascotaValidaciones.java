package com.veterniaria.ms_mascotas.service;

import com.veterniaria.ms_mascotas.DTO.MascotaDTO;
import com.veterniaria.ms_mascotas.model.Mascota;

public class MascotaValidaciones {
    
    public Boolean validarNullVacio(Mascota mascota) {
        if (mascota.getNombre() == null || mascota.getNombre().trim().length() == 0) {
            return false;
        }
        if (mascota.getColor() == null || mascota.getColor().trim().length() == 0) {
            return false;
        }
        if (mascota.getSexo() == null || mascota.getSexo().trim().length() == 0) {
            return false;
        }
        if (mascota.getDuenoId() == null) {
            return false;
        }
        if (mascota.getRaza() == null) {
            return false;
        }
        return true;
    }

    public MascotaDTO convertirADTO(Mascota mascota) {
        MascotaDTO dto = new MascotaDTO();
        dto.setId(mascota.getId());
        dto.setNombre(mascota.getNombre());
        dto.setColor(mascota.getColor());
        dto.setEdad(mascota.getEdad());
        dto.setFechaNacimiento(mascota.getFechaNacimiento());
        dto.setSexo(mascota.getSexo());
        dto.setDuenoId(mascota.getDuenoId());

        if (mascota.getRaza() != null) {
            dto.setRazaId(mascota.getRaza().getId());
            dto.setRazaNombre(mascota.getRaza().getNombre());
        }
        return dto;
    }
}
