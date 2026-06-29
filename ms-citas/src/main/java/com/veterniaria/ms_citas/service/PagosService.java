package com.veterniaria.ms_citas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterniaria.ms_citas.DTO.PagosDTO;
import com.veterniaria.ms_citas.model.Pagos;
import com.veterniaria.ms_citas.repository.PagosRepository;

@Service
public class PagosService {

    @Autowired
    private PagosValidaciones pagosValidaciones;

    @Autowired
    private PagosRepository pagosRepository;

    public List<PagosDTO> obtenerTodos() {
        List<PagosDTO> listaDTOs = new ArrayList<>();
        for (Pagos p : pagosRepository.findAll()) {
            listaDTOs.add(pagosValidaciones.convertirADTO(p));
        }
        return listaDTOs;
    }

    public PagosDTO buscarPorId(Integer id) {
        Pagos pagos = pagosRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Registro no encontrado!"));
        return pagosValidaciones.convertirADTO(pagos);
    }

    public PagosDTO guardar(Pagos nuevoPagos) {
        if (pagosValidaciones.validarNullVacio(nuevoPagos)) {
            Pagos guardado = pagosRepository.save(nuevoPagos);
            return pagosValidaciones.convertirADTO(guardado);
        }
        return null;
    }

    public String eliminar(Integer id) {
        try {
            Pagos pagos = pagosRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! Registro con ID " + id + " no existe."));
            pagosRepository.delete(pagos);
            return "Relación cita-pago eliminada exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}
