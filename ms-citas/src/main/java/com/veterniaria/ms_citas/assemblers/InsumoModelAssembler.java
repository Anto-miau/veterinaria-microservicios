package com.veterniaria.ms_citas.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.veterniaria.ms_citas.DTO.InsumoDTO;
import com.veterniaria.ms_citas.controller.v1.InsumoController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class InsumoModelAssembler implements RepresentationModelAssembler<InsumoDTO, EntityModel<InsumoDTO>>{

    @Override
    public EntityModel<InsumoDTO> toModel(InsumoDTO insumo) {
        return EntityModel.of(insumo,
                linkTo(methodOn(InsumoController.class).porId(insumo.getId())).withSelfRel(),
                linkTo(methodOn(InsumoController.class).todas()).withRel("insumos")
        );
    }
}
