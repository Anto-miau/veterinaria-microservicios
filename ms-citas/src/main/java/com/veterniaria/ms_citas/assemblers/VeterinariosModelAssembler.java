package com.veterniaria.ms_citas.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.veterniaria.ms_citas.DTO.VeterinariosDTO;
import com.veterniaria.ms_citas.controller.v1.VeterinariosController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VeterinariosModelAssembler implements RepresentationModelAssembler<VeterinariosDTO, EntityModel<VeterinariosDTO>>{

    @Override
    public EntityModel<VeterinariosDTO> toModel(VeterinariosDTO veterinarios) {
        return EntityModel.of(veterinarios,
                linkTo(methodOn(VeterinariosController.class).porId(veterinarios.getId())).withSelfRel(),
                linkTo(methodOn(VeterinariosController.class).todas()).withRel("veterinarios-cita")
        );
    }
}
