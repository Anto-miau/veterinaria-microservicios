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

import com.veterniaria.ms_mascotas.DTO.ContactosDTO;
import com.veterniaria.ms_mascotas.assemblers.ContactosModelAssembler;
import com.veterniaria.ms_mascotas.model.Contactos;
import com.veterniaria.ms_mascotas.service.ContactosService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import jakarta.validation.Valid;

@RestController("contactosControllerV2")
@RequestMapping("/api/v2/contactos-mascota")
public class ContactosController {

    @Autowired
    private ContactosService contactosService;

    @Autowired
    private ContactosModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ContactosDTO>>> todas() {
        List<EntityModel<ContactosDTO>> contactos = contactosService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (contactos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                contactos,
                linkTo(methodOn(ContactosController.class).todas()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ContactosDTO>> porId(@PathVariable Integer id) {
        try {
            ContactosDTO dto = contactosService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ContactosDTO>> registrar(@Valid @RequestBody Contactos contactos) {
        try {
            ContactosDTO nuevoContactos = contactosService.guardar(contactos);
            return ResponseEntity
                    .created(linkTo(methodOn(ContactosController.class).porId(nuevoContactos.getId())).toUri())
                    .body(assembler.toModel(nuevoContactos));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
