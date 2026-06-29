package com.veterniaria.ms_mascotas.service;

import org.springframework.stereotype.Service;

import com.veterniaria.ms_mascotas.DTO.ContactosDTO;
import com.veterniaria.ms_mascotas.model.Contactos;

@Service
public class ContactosValidaciones {
    
    public Boolean validarNullVacio(Contactos contactos) {
        if (contactos.getMascota() == null) {
            return false;
        }
        if (contactos.getContacto() == null) {
            return false;
        }
        return true;
    }

    public ContactosDTO convertirADTO(Contactos contactos) {
        ContactosDTO dto = new ContactosDTO();
        dto.setId(contactos.getId());

        if (contactos.getMascota() != null) {
            dto.setMascotaId(contactos.getMascota().getId());
            dto.setMascotaNombre(contactos.getMascota().getNombre());
        }
        if (contactos.getContacto() != null) {
            dto.setContactoId(contactos.getContacto().getId());
            dto.setContactoNombre(contactos.getContacto().getNombre());
        }
        return dto;
    }
}
