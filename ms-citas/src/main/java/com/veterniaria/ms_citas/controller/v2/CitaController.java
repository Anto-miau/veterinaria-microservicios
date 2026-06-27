package com.veterniaria.ms_citas.controller.v2;

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

import com.veterniaria.ms_citas.DTO.CitaDTO;
import com.veterniaria.ms_citas.assemblers.CitaModelAssembler;
import com.veterniaria.ms_citas.model.Cita;
import com.veterniaria.ms_citas.service.CitaService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController("citaControllerV2")
@RequestMapping("/api/v2/citas")
@Tag(name = "Cita v2", description = "CRUD de citas con HATEOAS")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private CitaModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<CitaDTO>>> todas() {
        List<EntityModel<CitaDTO>> citas = citaService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (citas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                citas,
                linkTo(methodOn(CitaController.class).todas()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<CitaDTO>> porId(@PathVariable Integer id) {
        try {
            CitaDTO dto = citaService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<CitaDTO>> registrar(@Valid @RequestBody Cita cita) {
        try {
            CitaDTO nuevaCita = citaService.guardar(cita);
            return ResponseEntity
                    .created(linkTo(methodOn(CitaController.class).porId(nuevaCita.getId())).toUri())
                    .body(assembler.toModel(nuevaCita));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
