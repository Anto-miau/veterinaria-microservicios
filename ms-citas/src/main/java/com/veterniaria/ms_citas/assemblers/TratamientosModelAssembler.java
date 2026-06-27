package com.veterniaria.ms_citas.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.veterniaria.ms_citas.DTO.TratamientosDTO;
import com.veterniaria.ms_citas.controller.v1.TratamientosController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TratamientosModelAssembler implements RepresentationModelAssembler<TratamientosDTO, EntityModel<TratamientosDTO>>{

    @Override
    public EntityModel<TratamientosDTO> toModel(TratamientosDTO tratamientos) {
        return EntityModel.of(tratamientos,
                linkTo(methodOn(TratamientosController.class).porId(tratamientos.getId())).withSelfRel(),
                linkTo(methodOn(TratamientosController.class).todas()).withRel("tratamientos-cita")
        );
    }
}
