package com.veterniaria.ms_citas.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.veterniaria.ms_citas.DTO.TratamientoDTO;
import com.veterniaria.ms_citas.controller.v1.TratamientoController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TratamientoModelAssembler implements RepresentationModelAssembler<TratamientoDTO, EntityModel<TratamientoDTO>>{

    @Override
    public EntityModel<TratamientoDTO> toModel(TratamientoDTO tratamiento) {
        return EntityModel.of(tratamiento,
                linkTo(methodOn(TratamientoController.class).porId(tratamiento.getId())).withSelfRel(),
                linkTo(methodOn(TratamientoController.class).todas()).withRel("tratamientos")
        );
    }

}
