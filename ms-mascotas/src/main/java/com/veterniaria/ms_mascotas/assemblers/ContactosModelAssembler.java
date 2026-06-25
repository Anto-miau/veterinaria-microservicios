package com.veterniaria.ms_mascotas.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.veterniaria.ms_mascotas.DTO.ContactosDTO;
import com.veterniaria.ms_mascotas.controller.v1.ContactosController;

@Component
public class ContactosModelAssembler implements RepresentationModelAssembler<ContactosDTO, EntityModel<ContactosDTO>> {

    @Override
    public EntityModel<ContactosDTO> toModel(ContactosDTO contactos) {
        return EntityModel.of(contactos,
                linkTo(methodOn(ContactosController.class).porId(contactos.getId())).withSelfRel(),
                linkTo(methodOn(ContactosController.class).todas()).withRel("contactos-mascota")
        );
    }
}
