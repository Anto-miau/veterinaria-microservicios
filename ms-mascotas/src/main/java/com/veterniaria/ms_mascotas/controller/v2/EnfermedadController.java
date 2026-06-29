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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.veterniaria.ms_mascotas.DTO.EnfermedadDTO;
import com.veterniaria.ms_mascotas.assemblers.EnfermedadModelAssembler;
import com.veterniaria.ms_mascotas.model.Enfermedad;
import com.veterniaria.ms_mascotas.service.EnfermedadService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController("enfermedadControllerV2")
@RequestMapping("/api/v2/enfermedades")
@Tag(name = "Enfermedad v2", description = "CRUD basico de enfermedad (HATEOAS)")
public class EnfermedadController {

    @Autowired
    private EnfermedadService enfermedadService;

    @Autowired
    private EnfermedadModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<EnfermedadDTO>>> todas() {
        List<EntityModel<EnfermedadDTO>> enfermedades = enfermedadService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (enfermedades.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                enfermedades,
                linkTo(methodOn(EnfermedadController.class).todas()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EnfermedadDTO>> porId(@PathVariable Integer id) {
        try {
            EnfermedadDTO dto = enfermedadService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EnfermedadDTO>> registrar(@Valid @RequestBody Enfermedad enfermedad) {
        try {
            EnfermedadDTO nuevaEnfermedad = enfermedadService.guardar(enfermedad);
            return ResponseEntity
                    .created(linkTo(methodOn(EnfermedadController.class).porId(nuevaEnfermedad.getId())).toUri())
                    .body(assembler.toModel(nuevaEnfermedad));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
