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

import com.veterniaria.ms_mascotas.DTO.ContactoDTO;
import com.veterniaria.ms_mascotas.assemblers.ContactoModelAssembler;
import com.veterniaria.ms_mascotas.model.Contacto;
import com.veterniaria.ms_mascotas.service.ContactoService;

import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import jakarta.validation.Valid;

@RestController("contactoControllerV2")
@RequestMapping("/api/v2/contactos")
@Tag(name = "Contacto v2", description = "CRUD basico de contacto (HATEOAS)")
public class ContactoController {

    @Autowired
    private ContactoService contactoService;

    @Autowired
    private ContactoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ContactoDTO>>> todas() {
        List<EntityModel<ContactoDTO>> contactos = contactoService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (contactos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                contactos,
                linkTo(methodOn(ContactoController.class).todas()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ContactoDTO>> porId(@PathVariable Integer id) {
        try {
            ContactoDTO dto = contactoService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ContactoDTO>> registrar(@Valid @RequestBody Contacto contacto) {
        try {
            ContactoDTO nuevoContacto = contactoService.guardar(contacto);
            return ResponseEntity
                    .created(linkTo(methodOn(ContactoController.class).porId(nuevoContacto.getId())).toUri())
                    .body(assembler.toModel(nuevoContacto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
