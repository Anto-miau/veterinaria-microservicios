package com.veterniaria.ms_citas.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.veterniaria.ms_citas.DTO.CitaDTO;
import com.veterniaria.ms_citas.controller.v1.CitaController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CitaModelAssembler implements RepresentationModelAssembler<CitaDTO,EntityModel<CitaDTO>>{

    @Override
    public EntityModel<CitaDTO> toModel(CitaDTO cita) {
        return EntityModel.of(cita,
                linkTo(methodOn(CitaController.class).porId(cita.getId())).withSelfRel(),
                linkTo(methodOn(CitaController.class).todas()).withRel("citas")
        );
    }
}
