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

import com.veterniaria.ms_mascotas.DTO.MascotaDTO;
import com.veterniaria.ms_mascotas.assemblers.MascotaModelAssembler;
import com.veterniaria.ms_mascotas.model.Mascota;
import com.veterniaria.ms_mascotas.service.MascotaService;

import jakarta.validation.Valid;

@RestController("mascotaControllerV2")
@RequestMapping("/api/v2/mascotas")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    @Autowired
    private MascotaModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<MascotaDTO>>> todas() {
        List<EntityModel<MascotaDTO>> mascotas = mascotaService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (mascotas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                mascotas,
                linkTo(methodOn(MascotaController.class).todas()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<MascotaDTO>> porId(@PathVariable Integer id) {
        try {
            MascotaDTO dto = mascotaService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<MascotaDTO>> registrar(@Valid @RequestBody Mascota mascota) {
        try {
            MascotaDTO nuevaMascota = mascotaService.guardar(mascota);
            return ResponseEntity
                    .created(linkTo(methodOn(MascotaController.class).porId(nuevaMascota.getId())).toUri())
                    .body(assembler.toModel(nuevaMascota));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
