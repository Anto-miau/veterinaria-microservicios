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

import com.veterniaria.ms_citas.DTO.TratamientosDTO;
import com.veterniaria.ms_citas.assemblers.TratamientosModelAssembler;
import com.veterniaria.ms_citas.model.Tratamientos;
import com.veterniaria.ms_citas.service.TratamientosService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController("tratamientosControllerV2")
@RequestMapping("/api/v2/tratamientos-cita")
@Tag(name = "Tratamientos-Cita v2", description = "Relación cita-tratamiento con HATEOAS")
public class TratamientosController {

    @Autowired
    private TratamientosService tratamientosService;

    @Autowired
    private TratamientosModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<TratamientosDTO>>> todas() {
        List<EntityModel<TratamientosDTO>> tratamientos = tratamientosService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (tratamientos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                tratamientos,
                linkTo(methodOn(TratamientosController.class).todas()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<TratamientosDTO>> porId(@PathVariable Integer id) {
        try {
            TratamientosDTO dto = tratamientosService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<TratamientosDTO>> registrar(@Valid @RequestBody Tratamientos tratamientos) {
        try {
            TratamientosDTO nuevo = tratamientosService.guardar(tratamientos);
            return ResponseEntity
                    .created(linkTo(methodOn(TratamientosController.class).porId(nuevo.getId())).toUri())
                    .body(assembler.toModel(nuevo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
