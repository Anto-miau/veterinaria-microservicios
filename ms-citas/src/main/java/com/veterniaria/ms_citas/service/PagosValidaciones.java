package com.veterniaria.ms_citas.service;

import org.springframework.stereotype.Service;

import com.veterniaria.ms_citas.DTO.PagosDTO;
import com.veterniaria.ms_citas.model.Pagos;

@Service
public class PagosValidaciones {

    public Boolean validarNullVacio(Pagos pagos) {
        if (pagos.getCita() == null) {
            return false;
        }
        if (pagos.getPago() == null) {
            return false;
        }
        return true;
    }

    public PagosDTO convertirADTO(Pagos pagos) {
        PagosDTO dto = new PagosDTO();
        dto.setId(pagos.getId());

        if (pagos.getCita() != null) {
            dto.setCitaId(pagos.getCita().getId());
            dto.setCitaMotivo(pagos.getCita().getMotivo());
        }
        if (pagos.getPago() != null) {
            dto.setPagoId(pagos.getPago().getId());
            dto.setPagoNombre(pagos.getPago().getNombre());
        }
        return dto;
    }
}
