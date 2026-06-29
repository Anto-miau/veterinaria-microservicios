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

import com.veterniaria.ms_citas.DTO.InsumoDTO;
import com.veterniaria.ms_citas.assemblers.InsumoModelAssembler;
import com.veterniaria.ms_citas.model.Insumo;
import com.veterniaria.ms_citas.service.InsumoService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController("insumoControllerV2")
@RequestMapping("/api/v2/insumos")
@Tag(name = "Insumo v2", description = "CRUD de insumos con HATEOAS")
public class InsumoController {

    @Autowired
    private InsumoService insumoService;

    @Autowired
    private InsumoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<InsumoDTO>>> todas() {
        List<EntityModel<InsumoDTO>> insumos = insumoService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (insumos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                insumos,
                linkTo(methodOn(InsumoController.class).todas()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<InsumoDTO>> porId(@PathVariable Integer id) {
        try {
            InsumoDTO dto = insumoService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<InsumoDTO>> registrar(@Valid @RequestBody Insumo insumo) {
        try {
            InsumoDTO nuevoInsumo = insumoService.guardar(insumo);
            return ResponseEntity
                    .created(linkTo(methodOn(InsumoController.class).porId(nuevoInsumo.getId())).toUri())
                    .body(assembler.toModel(nuevoInsumo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
