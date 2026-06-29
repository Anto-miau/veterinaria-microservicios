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

import com.veterniaria.ms_citas.DTO.PagoDTO;
import com.veterniaria.ms_citas.assemblers.PagoModelAssembler;
import com.veterniaria.ms_citas.model.Pago;
import com.veterniaria.ms_citas.service.PagoService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController("pagoControllerV2")
@RequestMapping("/api/v2/pagos")
@Tag(name = "Pago v2", description = "CRUD de métodos de pago con HATEOAS")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private PagoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<PagoDTO>>> todas() {
        List<EntityModel<PagoDTO>> pagos = pagoService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (pagos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                pagos,
                linkTo(methodOn(PagoController.class).todas()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PagoDTO>> porId(@PathVariable Integer id) {
        try {
            PagoDTO dto = pagoService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PagoDTO>> registrar(@Valid @RequestBody Pago pago) {
        try {
            PagoDTO nuevoPago = pagoService.guardar(pago);
            return ResponseEntity
                    .created(linkTo(methodOn(PagoController.class).porId(nuevoPago.getId())).toUri())
                    .body(assembler.toModel(nuevoPago));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
