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

import com.veterniaria.ms_citas.DTO.VeterinariosDTO;
import com.veterniaria.ms_citas.assemblers.VeterinariosModelAssembler;
import com.veterniaria.ms_citas.model.Veterinarios;
import com.veterniaria.ms_citas.service.VeterinariosService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController("veterinariosControllerV2")
@RequestMapping("/api/v2/veterinarios-cita")
@Tag(name = "Veterinarios-Cita v2", description = "Relación cita-veterinario con HATEOAS")
public class VeterinariosController {

    @Autowired
    private VeterinariosService veterinariosService;

    @Autowired
    private VeterinariosModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<VeterinariosDTO>>> todas() {
        List<EntityModel<VeterinariosDTO>> veterinarios = veterinariosService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (veterinarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                veterinarios,
                linkTo(methodOn(VeterinariosController.class).todas()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<VeterinariosDTO>> porId(@PathVariable Integer id) {
        try {
            VeterinariosDTO dto = veterinariosService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<VeterinariosDTO>> registrar(@Valid @RequestBody Veterinarios veterinarios) {
        try {
            VeterinariosDTO nuevo = veterinariosService.guardar(veterinarios);
            return ResponseEntity
                    .created(linkTo(methodOn(VeterinariosController.class).porId(nuevo.getId())).toUri())
                    .body(assembler.toModel(nuevo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
