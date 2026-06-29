package com.veterniaria.ms_citas.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.veterniaria.ms_citas.DTO.InsumoDTO;
import com.veterniaria.ms_citas.DTO.InsumosDTO;
import com.veterniaria.ms_citas.controller.v1.InsumosController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class InsumosModelAssembler implements RepresentationModelAssembler<InsumosDTO, EntityModel<InsumosDTO>>{

    @Override
    public EntityModel<InsumosDTO> toModel(InsumosDTO insumos) {
        return EntityModel.of(insumos,
                linkTo(methodOn(InsumosController.class).porId(insumos.getId())).withSelfRel(),
                linkTo(methodOn(InsumosController.class).todas()).withRel("insumos-tratamiento")
        );
    }
}
