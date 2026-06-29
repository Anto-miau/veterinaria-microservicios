package com.veterniaria.ms_mascotas.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.veterniaria.ms_mascotas.DTO.EnfermedadesDTO;
import com.veterniaria.ms_mascotas.controller.v1.EnfermedadesController;

@Component
public class EnfermedadesModelAssembler implements RepresentationModelAssembler<EnfermedadesDTO, EntityModel<EnfermedadesDTO>> {

    @Override
    public EntityModel<EnfermedadesDTO> toModel(EnfermedadesDTO enfermedades) {
        return EntityModel.of(enfermedades,
                linkTo(methodOn(EnfermedadesController.class).porId(enfermedades.getId())).withSelfRel(),
                linkTo(methodOn(EnfermedadesController.class).todas()).withRel("enfermedades-mascota")
        );
    }
}
