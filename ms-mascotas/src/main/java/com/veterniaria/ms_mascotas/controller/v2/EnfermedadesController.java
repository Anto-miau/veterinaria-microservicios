package com.veterniaria.ms_mascotas.controller.v2;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.veterniaria.ms_mascotas.DTO.EnfermedadesDTO;
import com.veterniaria.ms_mascotas.assemblers.EnfermedadesModelAssembler;
import com.veterniaria.ms_mascotas.model.Enfermedades;
import com.veterniaria.ms_mascotas.service.EnfermedadesService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import jakarta.validation.Valid;

@RestController("enfermedadesControllerV2")
@RequestMapping("/api/v2/enfermedades-mascota")
public class EnfermedadesController {
    @Autowired
    private EnfermedadesService enfermedadesService;

    @Autowired
    private EnfermedadesModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<EnfermedadesDTO>>> todas() {
        List<EntityModel<EnfermedadesDTO>> enfermedades = enfermedadesService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (enfermedades.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                enfermedades,
                linkTo(methodOn(EnfermedadesController.class).todas()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EnfermedadesDTO>> porId(@PathVariable Integer id) {
        try {
            EnfermedadesDTO dto = enfermedadesService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EnfermedadesDTO>> registrar(@Valid @RequestBody Enfermedades enfermedades) {
        try {
            EnfermedadesDTO nuevoEnfermedades = enfermedadesService.guardar(enfermedades);
            return ResponseEntity
                    .created(linkTo(methodOn(EnfermedadesController.class).porId(nuevoEnfermedades.getId())).toUri())
                    .body(assembler.toModel(nuevoEnfermedades));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
