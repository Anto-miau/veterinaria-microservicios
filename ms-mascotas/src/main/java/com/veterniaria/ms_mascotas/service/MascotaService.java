package com.veterniaria.ms_mascotas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterniaria.ms_mascotas.DTO.MascotaDTO;
import com.veterniaria.ms_mascotas.model.Mascota;
import com.veterniaria.ms_mascotas.repository.MascotaRepository;

@Service
public class MascotaService {
    
    @Autowired
    private MascotaValidaciones mascotaValidaciones;

    @Autowired
    private MascotaRepository mascotaRepository;

    public List<MascotaDTO> obtenerTodos() {
        List<MascotaDTO> listaDTOs = new ArrayList<>();
        for (Mascota m : mascotaRepository.findAll()) {
            listaDTOs.add(mascotaValidaciones.convertirADTO(m));
        }
        return listaDTOs;
    }

    public MascotaDTO buscarPorId(Integer id) {
        Mascota mascota = mascotaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Mascota no encontrada!"));
        return mascotaValidaciones.convertirADTO(mascota);
    }

    public MascotaDTO guardar(Mascota nuevaMascota) {
        if (mascotaValidaciones.validarNullVacio(nuevaMascota)) {
            Mascota guardada = mascotaRepository.save(nuevaMascota);
            return mascotaValidaciones.convertirADTO(guardada);
        }
        return null;
    }

    public MascotaDTO actualizar(Integer id, Mascota mascotaActualizada) {
        Mascota mascota = mascotaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Mascota no existe en los registros!"));

        if (mascotaActualizada.getNombre() != null) {
            mascota.setNombre(mascotaActualizada.getNombre());
        }
        if (mascotaActualizada.getColor() != null) {
            mascota.setColor(mascotaActualizada.getColor());
        }
        if (mascotaActualizada.getEdad() != null) {
            mascota.setEdad(mascotaActualizada.getEdad());
        }
        if (mascotaActualizada.getFechaNacimiento() != null) {
            mascota.setFechaNacimiento(mascotaActualizada.getFechaNacimiento());
        }
        if (mascotaActualizada.getSexo() != null) {
            mascota.setSexo(mascotaActualizada.getSexo());
        }
        if (mascotaActualizada.getDuenoId() != null) {
            mascota.setDuenoId(mascotaActualizada.getDuenoId());
        }
        if (mascotaActualizada.getRaza() != null) {
            mascota.setRaza(mascotaActualizada.getRaza());
        }

        return mascotaValidaciones.convertirADTO(mascotaRepository.save(mascota));
    }

    public String eliminar(Integer id) {
        try {
            Mascota mascota = mascotaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! Mascota con ID " + id + " no existe."));
            mascotaRepository.delete(mascota);
            return "Mascota '" + mascota.getNombre() + "' ha sido eliminada exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}
