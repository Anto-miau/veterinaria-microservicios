package com.veterniaria.ms_mascotas.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.veterniaria.ms_mascotas.DTO.MascotaDTO;
import com.veterniaria.ms_mascotas.controller.v2.MascotaController;

@Component
public class MascotaModelAssembler implements RepresentationModelAssembler<MascotaDTO, EntityModel<MascotaDTO>> {

    @Override
    public EntityModel<MascotaDTO> toModel(MascotaDTO mascota) {
        return EntityModel.of(mascota,
                linkTo(methodOn(MascotaController.class).porId(mascota.getId())).withSelfRel(),
                linkTo(methodOn(MascotaController.class).todas()).withRel("mascotas")
        );
    }
}
