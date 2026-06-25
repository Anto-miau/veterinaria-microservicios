package com.veterniaria.ms_mascotas.controller.v2;

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

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;

import com.veterniaria.ms_mascotas.DTO.EspecieDTO;
import com.veterniaria.ms_mascotas.assemblers.EspecieModelAssembler;
import com.veterniaria.ms_mascotas.model.Especie;
import com.veterniaria.ms_mascotas.service.EspecieService;

import jakarta.validation.Valid;

@RestController("especieControllerV2")
@RequestMapping("/api/v2/especies")
public class EspecieController {
    
    @Autowired
    private EspecieService especieService;

    @Autowired
    private EspecieModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<EspecieDTO>>> todas() {
        List<EntityModel<EspecieDTO>> especies = especieService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (especies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                especies,
                linkTo(methodOn(EspecieController.class).todas()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EspecieDTO>> porId(@PathVariable Integer id) {
        try {
            EspecieDTO dto = especieService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EspecieDTO>> registrar(@Valid @RequestBody Especie especie) {
        try {
            EspecieDTO nuevaEspecie = especieService.guardar(especie);
            return ResponseEntity
                    .created(linkTo(methodOn(EspecieController.class).porId(nuevaEspecie.getId())).toUri())
                    .body(assembler.toModel(nuevaEspecie));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
