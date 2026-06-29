package com.veterniaria.ms_mascotas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterniaria.ms_mascotas.DTO.ContactosDTO;
import com.veterniaria.ms_mascotas.model.Contactos;
import com.veterniaria.ms_mascotas.repository.ContactosRepository;

@Service
public class ContactosService {
    
    @Autowired
    private ContactosValidaciones contactosValidaciones;

    @Autowired
    private ContactosRepository contactosRepository;

    public List<ContactosDTO> obtenerTodos() {
        List<ContactosDTO> listaDTOs = new ArrayList<>();
        for (Contactos c : contactosRepository.findAll()) {
            listaDTOs.add(contactosValidaciones.convertirADTO(c));
        }
        return listaDTOs;
    }

    public ContactosDTO buscarPorId(Integer id) {
        Contactos contactos = contactosRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Registro no encontrado!"));
        return contactosValidaciones.convertirADTO(contactos);
    }

    public ContactosDTO guardar(Contactos nuevoContactos) {
        if (contactosValidaciones.validarNullVacio(nuevoContactos)) {
            Contactos guardado = contactosRepository.save(nuevoContactos);
            return contactosValidaciones.convertirADTO(guardado);
        }
        return null;
    }

    public String eliminar(Integer id) {
        try {
            Contactos contactos = contactosRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! Registro con ID " + id + " no existe."));
            contactosRepository.delete(contactos);
            return "Relación contacto-mascota eliminada exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}
