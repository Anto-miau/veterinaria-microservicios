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

import com.veterniaria.ms_mascotas.DTO.RazaDTO;
import com.veterniaria.ms_mascotas.assemblers.RazaModelAssembler;
import com.veterniaria.ms_mascotas.model.Raza;
import com.veterniaria.ms_mascotas.service.RazaService;

import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import jakarta.validation.Valid;

@RestController("razaControllerV2")
@RequestMapping("/api/v2/razas")
@Tag(name = "Raza v2", description = "CRUD basico de raza (HATEOAS)")
public class RazaController {

    @Autowired
    private RazaService razaService;

    @Autowired
    private RazaModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<RazaDTO>>> todas() {
        List<EntityModel<RazaDTO>> razas = razaService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (razas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                razas,
                linkTo(methodOn(RazaController.class).todas()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<RazaDTO>> porId(@PathVariable Integer id) {
        try {
            RazaDTO dto = razaService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<RazaDTO>> registrar(@Valid @RequestBody Raza raza) {
        try {
            RazaDTO nuevaRaza = razaService.guardar(raza);
            return ResponseEntity
                    .created(linkTo(methodOn(RazaController.class).porId(nuevaRaza.getId())).toUri())
                    .body(assembler.toModel(nuevaRaza));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
