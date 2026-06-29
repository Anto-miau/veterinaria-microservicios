package com.veterniaria.ms_citas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterniaria.ms_citas.DTO.CitaDTO;
import com.veterniaria.ms_citas.model.Cita;
import com.veterniaria.ms_citas.repository.CitaRepository;

@Service
public class CitaService {

    @Autowired
    private CitaValidaciones citaValidaciones;

    @Autowired
    private CitaRepository citaRepository;

    public List<CitaDTO> obtenerTodos() {
        List<CitaDTO> listaDTOs = new ArrayList<>();
        for (Cita c : citaRepository.findAll()) {
            listaDTOs.add(citaValidaciones.convertirADTO(c));
        }
        return listaDTOs;
    }

    public CitaDTO buscarPorId(Integer id) {
        Cita cita = citaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Cita no encontrada!"));
        return citaValidaciones.convertirADTO(cita);
    }

    public CitaDTO guardar(Cita nuevaCita) {
        if (citaValidaciones.validarNullVacio(nuevaCita)) {
            Cita guardada = citaRepository.save(nuevaCita);
            return citaValidaciones.convertirADTO(guardada);
        }
        return null;
    }

    public CitaDTO actualizar(Integer id, Cita citaActualizada) {
        Cita cita = citaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Cita no existe en los registros!"));

        if (citaActualizada.getMotivo() != null) {
            cita.setMotivo(citaActualizada.getMotivo());
        }
        if (citaActualizada.getFechaHora() != null) {
            cita.setFechaHora(citaActualizada.getFechaHora());
        }
        if (citaActualizada.getEstado() != null) {
            cita.setEstado(citaActualizada.getEstado());
        }
        if (citaActualizada.getMascotaId() != null) {
            cita.setMascotaId(citaActualizada.getMascotaId());
        }

        return citaValidaciones.convertirADTO(citaRepository.save(cita));
    }

    public String eliminar(Integer id) {
        try {
            Cita cita = citaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! Cita con ID " + id + " no existe."));
            citaRepository.delete(cita);
            return "Cita '" + cita.getMotivo() + "' ha sido eliminada exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}
