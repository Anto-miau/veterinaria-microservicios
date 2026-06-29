package com.veterniaria.ms_mascotas.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.veterniaria.ms_mascotas.DTO.RazaDTO;
import com.veterniaria.ms_mascotas.controller.v1.RazaController;

@Component
public class RazaModelAssembler implements RepresentationModelAssembler<RazaDTO, EntityModel<RazaDTO>>{

    @Override
    public EntityModel<RazaDTO> toModel(RazaDTO raza) {
        return EntityModel.of(raza,
                linkTo(methodOn(RazaController.class).porId(raza.getId())).withSelfRel(),
                linkTo(methodOn(RazaController.class).todas()).withRel("razas")
        );
    }
}
