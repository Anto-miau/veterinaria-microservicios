package com.veterniaria.ms_mascotas.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.veterniaria.ms_mascotas.DTO.EspecieDTO;
import com.veterniaria.ms_mascotas.controller.v1.EspecieController;

@Component
public class EspecieModelAssembler implements RepresentationModelAssembler<EspecieDTO, EntityModel<EspecieDTO>> {
    @Override
    public EntityModel<EspecieDTO> toModel(EspecieDTO especie) {
        return EntityModel.of(especie,
                linkTo(methodOn(EspecieController.class).porId(especie.getId())).withSelfRel(),
                linkTo(methodOn(EspecieController.class).todas()).withRel("especies")
        );
    }
}
