package com.veterniaria.ms_mascotas.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.veterniaria.ms_mascotas.DTO.EnfermedadDTO;
import com.veterniaria.ms_mascotas.controller.v1.EnfermedadController;

@Component
public class EnfermedadModelAssembler implements RepresentationModelAssembler<EnfermedadDTO, EntityModel<EnfermedadDTO>>{

    @Override
    public EntityModel<EnfermedadDTO> toModel(EnfermedadDTO enfermedad) {
        return EntityModel.of(enfermedad,
                linkTo(methodOn(EnfermedadController.class).porId(enfermedad.getId())).withSelfRel(),
                linkTo(methodOn(EnfermedadController.class).todas()).withRel("enfermedades")
        );
    }
}
