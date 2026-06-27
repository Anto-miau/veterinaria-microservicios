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

import com.veterniaria.ms_citas.DTO.TratamientoDTO;
import com.veterniaria.ms_citas.assemblers.TratamientoModelAssembler;
import com.veterniaria.ms_citas.model.Tratamiento;
import com.veterniaria.ms_citas.service.TratamientoService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController("tratamientoControllerV2")
@RequestMapping("/api/v2/tratamientos")
@Tag(name = "Tratamiento v2", description = "CRUD de tratamientos con HATEOAS")
public class TratamientoController {

    @Autowired
    private TratamientoService tratamientoService;

    @Autowired
    private TratamientoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<TratamientoDTO>>> todas() {
        List<EntityModel<TratamientoDTO>> tratamientos = tratamientoService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (tratamientos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                tratamientos,
                linkTo(methodOn(TratamientoController.class).todas()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<TratamientoDTO>> porId(@PathVariable Integer id) {
        try {
            TratamientoDTO dto = tratamientoService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<TratamientoDTO>> registrar(@Valid @RequestBody Tratamiento tratamiento) {
        try {
            TratamientoDTO nuevoTratamiento = tratamientoService.guardar(tratamiento);
            return ResponseEntity
                    .created(linkTo(methodOn(TratamientoController.class).porId(nuevoTratamiento.getId())).toUri())
                    .body(assembler.toModel(nuevoTratamiento));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
