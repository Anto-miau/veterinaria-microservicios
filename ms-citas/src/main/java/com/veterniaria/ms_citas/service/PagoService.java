package com.veterniaria.ms_citas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterniaria.ms_citas.DTO.PagoDTO;
import com.veterniaria.ms_citas.model.Pago;
import com.veterniaria.ms_citas.repository.PagoRepository;

@Service
public class PagoService {

    @Autowired
    private PagoValidaciones pagoValidaciones;

    @Autowired
    private PagoRepository pagoRepository;

    public List<PagoDTO> obtenerTodos() {
        List<PagoDTO> listaDTOs = new ArrayList<>();
        for (Pago p : pagoRepository.findAll()) {
            listaDTOs.add(pagoValidaciones.convertirADTO(p));
        }
        return listaDTOs;
    }

    public PagoDTO buscarPorId(Integer id) {
        Pago pago = pagoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Método de pago no encontrado!"));
        return pagoValidaciones.convertirADTO(pago);
    }

    public PagoDTO guardar(Pago nuevoPago) {
        if (pagoValidaciones.validarNullVacio(nuevoPago)) {
            Pago guardado = pagoRepository.save(nuevoPago);
            return pagoValidaciones.convertirADTO(guardado);
        }
        return null;
    }

    public PagoDTO actualizar(Integer id, Pago pagoActualizado) {
        Pago pago = pagoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Método de pago no existe en los registros!"));

        if (pagoActualizado.getNombre() != null) {
            pago.setNombre(pagoActualizado.getNombre());
        }

        return pagoValidaciones.convertirADTO(pagoRepository.save(pago));
    }

    public String eliminar(Integer id) {
        try {
            Pago pago = pagoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! Método de pago con ID " + id + " no existe."));
            pagoRepository.delete(pago);
            return "Método de pago '" + pago.getNombre() + "' ha sido eliminado exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}
