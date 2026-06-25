package com.veterniaria.ms_mascotas.controller.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.veterniaria.ms_mascotas.DTO.ContactosDTO;
import com.veterniaria.ms_mascotas.model.Contactos;
import com.veterniaria.ms_mascotas.service.ContactosService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/contactos-mascota")
@Tag(name = "Contactos v1", description = "CRUD basico de contactos (sin HATEOAS)")
public class ContactosController {
    
    @Autowired
    private ContactosService contactosService;

    @GetMapping
    public ResponseEntity<List<ContactosDTO>> todas() {
        List<ContactosDTO> lista = contactosService.obtenerTodos();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> porId(@PathVariable Integer id) {
        try {
            ContactosDTO dto = contactosService.buscarPorId(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody Contactos contactos) {
        try {
            ContactosDTO dto = contactosService.guardar(contactos);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error en la transmisión de datos", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        String resultado = contactosService.eliminar(id);
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }
}
