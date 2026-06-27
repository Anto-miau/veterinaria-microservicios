package com.veterniaria.ms_citas.service;

import org.springframework.stereotype.Service;

import com.veterniaria.ms_citas.DTO.TratamientosDTO;
import com.veterniaria.ms_citas.model.Tratamientos;

@Service
public class TratamientosValidaciones {

    public Boolean validarNullVacio(Tratamientos tratamientos) {
        if (tratamientos.getCita() == null) {
            return false;
        }
        if (tratamientos.getTratamiento() == null) {
            return false;
        }
        return true;
    }

    public TratamientosDTO convertirADTO(Tratamientos tratamientos) {
        TratamientosDTO dto = new TratamientosDTO();
        dto.setId(tratamientos.getId());

        if (tratamientos.getCita() != null) {
            dto.setCitaId(tratamientos.getCita().getId());
            dto.setCitaMotivo(tratamientos.getCita().getMotivo());
        }
        if (tratamientos.getTratamiento() != null) {
            dto.setTratamientoId(tratamientos.getTratamiento().getId());
            dto.setTratamientoDiagnostico(tratamientos.getTratamiento().getDiagnostico());
        }
        return dto;
    }
}
