package com.veterniaria.ms_mascotas.service;

import org.springframework.stereotype.Service;

import com.veterniaria.ms_mascotas.DTO.ContactoDTO;
import com.veterniaria.ms_mascotas.model.Contacto;

@Service
public class ContactoValidaciones {

    public Boolean validarNullVacio(Contacto contacto) {
        if (contacto.getNombre() == null || contacto.getNombre().trim().length() == 0) {
            return false;
        }
        if (contacto.getTelefono() == null || contacto.getTelefono().trim().length() == 0) {
            return false;
        }
        if (contacto.getCorreo() == null || contacto.getCorreo().trim().length() == 0) {
            return false;
        }
        return true;
    }

    public ContactoDTO convertirADTO(Contacto contacto) {
        ContactoDTO dto = new ContactoDTO();
        dto.setId(contacto.getId());
        dto.setNombre(contacto.getNombre());
        dto.setTelefono(contacto.getTelefono());
        dto.setCorreo(contacto.getCorreo());
        return dto;
    }
}
