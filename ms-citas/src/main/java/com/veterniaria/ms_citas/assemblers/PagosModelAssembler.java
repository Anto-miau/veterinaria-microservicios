package com.veterniaria.ms_citas.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.veterniaria.ms_citas.DTO.PagosDTO;
import com.veterniaria.ms_citas.controller.v1.PagosController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PagosModelAssembler implements RepresentationModelAssembler<PagosDTO, EntityModel<PagosDTO>>{

    @Override
    public EntityModel<PagosDTO> toModel(PagosDTO pagos) {
        return EntityModel.of(pagos,
                linkTo(methodOn(PagosController.class).porId(pagos.getId())).withSelfRel(),
                linkTo(methodOn(PagosController.class).todas()).withRel("pagos-cita")
        );
    }
}
