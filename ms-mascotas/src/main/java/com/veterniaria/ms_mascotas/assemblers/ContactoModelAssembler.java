package com.veterniaria.ms_mascotas.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.veterniaria.ms_mascotas.DTO.ContactoDTO;
import com.veterniaria.ms_mascotas.controller.v2.ContactoController;

@Component
public class ContactoModelAssembler implements RepresentationModelAssembler<ContactoDTO, EntityModel<ContactoDTO>> {

    @Override
    public EntityModel<ContactoDTO> toModel(ContactoDTO contacto) {
        return EntityModel.of(contacto,
                linkTo(methodOn(ContactoController.class).porId(contacto.getId())).withSelfRel(),
                linkTo(methodOn(ContactoController.class).todas()).withRel("contactos")
        );
    }
}
