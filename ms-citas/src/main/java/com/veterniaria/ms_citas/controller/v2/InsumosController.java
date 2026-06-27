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

import com.veterniaria.ms_citas.DTO.InsumosDTO;
import com.veterniaria.ms_citas.assemblers.InsumosModelAssembler;
import com.veterniaria.ms_citas.model.Insumos;
import com.veterniaria.ms_citas.service.InsumosService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController("insumosControllerV2")
@RequestMapping("/api/v2/insumos-tratamiento")
@Tag(name = "Insumos-Tratamiento v2", description = "Relación tratamiento-insumo con HATEOAS")
public class InsumosController {

    @Autowired
    private InsumosService insumosService;

    @Autowired
    private InsumosModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<InsumosDTO>>> todas() {
        List<EntityModel<InsumosDTO>> insumos = insumosService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (insumos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                insumos,
                linkTo(methodOn(InsumosController.class).todas()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<InsumosDTO>> porId(@PathVariable Integer id) {
        try {
            InsumosDTO dto = insumosService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<InsumosDTO>> registrar(@Valid @RequestBody Insumos insumos) {
        try {
            InsumosDTO nuevo = insumosService.guardar(insumos);
            return ResponseEntity
                    .created(linkTo(methodOn(InsumosController.class).porId(nuevo.getId())).toUri())
                    .body(assembler.toModel(nuevo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
