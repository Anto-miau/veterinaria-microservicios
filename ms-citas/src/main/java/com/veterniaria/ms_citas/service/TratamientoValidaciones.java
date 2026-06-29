package com.veterniaria.ms_citas.service;

import org.springframework.stereotype.Service;

import com.veterniaria.ms_citas.DTO.TratamientoDTO;
import com.veterniaria.ms_citas.model.Tratamiento;

@Service
public class TratamientoValidaciones {

    public Boolean validarNullVacio(Tratamiento tratamiento) {
        if (tratamiento.getDiagnostico() == null || tratamiento.getDiagnostico().trim().length() == 0) {
            return false;
        }
        if (tratamiento.getDescripcion() == null || tratamiento.getDescripcion().trim().length() == 0) {
            return false;
        }
        if (tratamiento.getFechaInicio() == null) {
            return false;
        }
        if (tratamiento.getFechaTermino() == null) {
            return false;
        }
        return true;
    }

    public TratamientoDTO convertirADTO(Tratamiento tratamiento) {
        TratamientoDTO dto = new TratamientoDTO();
        dto.setId(tratamiento.getId());
        dto.setDiagnostico(tratamiento.getDiagnostico());
        dto.setDescripcion(tratamiento.getDescripcion());
        dto.setFechaInicio(tratamiento.getFechaInicio());
        dto.setFechaTermino(tratamiento.getFechaTermino());
        return dto;
    }
}
