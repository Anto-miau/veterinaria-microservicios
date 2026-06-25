package com.veterniaria.ms_mascotas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterniaria.ms_mascotas.DTO.ContactoDTO;
import com.veterniaria.ms_mascotas.model.Contacto;
import com.veterniaria.ms_mascotas.repository.ContactoRepository;

@Service
public class ContactoService {
    
    @Autowired
    private ContactoValidaciones contactoValidaciones;

    @Autowired
    private ContactoRepository contactoRepository;

    public List<ContactoDTO> obtenerTodos() {
        List<ContactoDTO> listaDTOs = new ArrayList<>();
        for (Contacto c : contactoRepository.findAll()) {
            listaDTOs.add(contactoValidaciones.convertirADTO(c));
        }
        return listaDTOs;
    }

    public ContactoDTO buscarPorId(Integer id) {
        Contacto contacto = contactoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Contacto no encontrado!"));
        return contactoValidaciones.convertirADTO(contacto);
    }

    public ContactoDTO guardar(Contacto nuevoContacto) {
        if (contactoValidaciones.validarNullVacio(nuevoContacto)) {
            Contacto guardado = contactoRepository.save(nuevoContacto);
            return contactoValidaciones.convertirADTO(guardado);
        }
        return null;
    }

    public ContactoDTO actualizar(Integer id, Contacto contactoActualizado) {
        Contacto contacto = contactoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Contacto no existe en los registros!"));

        if (contactoActualizado.getNombre() != null) {
            contacto.setNombre(contactoActualizado.getNombre());
        }
        if (contactoActualizado.getTelefono() != null) {
            contacto.setTelefono(contactoActualizado.getTelefono());
        }
        if (contactoActualizado.getCorreo() != null) {
            contacto.setCorreo(contactoActualizado.getCorreo());
        }

        return contactoValidaciones.convertirADTO(contactoRepository.save(contacto));
    }

    public String eliminar(Integer id) {
        try {
            Contacto contacto = contactoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! Contacto con ID " + id + " no existe."));
            contactoRepository.delete(contacto);
            return "Contacto '" + contacto.getNombre() + "' ha sido eliminado exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}
