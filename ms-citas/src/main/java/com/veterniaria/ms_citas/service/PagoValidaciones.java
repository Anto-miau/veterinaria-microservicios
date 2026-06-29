package com.veterniaria.ms_citas.service;

import org.springframework.stereotype.Service;

import com.veterniaria.ms_citas.DTO.PagoDTO;
import com.veterniaria.ms_citas.model.Pago;

@Service
public class PagoValidaciones {

    public Boolean validarNullVacio(Pago pago) {
        if (pago.getNombre() == null || pago.getNombre().trim().length() == 0) {
            return false;
        }
        return true;
    }

    public PagoDTO convertirADTO(Pago pago) {
        PagoDTO dto = new PagoDTO();
        dto.setId(pago.getId());
        dto.setNombre(pago.getNombre());
        return dto;
    }
}
