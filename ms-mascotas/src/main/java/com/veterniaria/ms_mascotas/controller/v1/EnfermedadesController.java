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

import com.veterniaria.ms_mascotas.DTO.EnfermedadesDTO;
import com.veterniaria.ms_mascotas.model.Enfermedades;
import com.veterniaria.ms_mascotas.service.EnfermedadesService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/enfermedades-mascota")
@Tag(name = "Enfermedades v1", description = "CRUD basico de enfermedades (sin HATEOAS)")
public class EnfermedadesController {

    @Autowired
    private EnfermedadesService enfermedadesService;

    @GetMapping
    public ResponseEntity<List<EnfermedadesDTO>> todas() {
        List<EnfermedadesDTO> lista = enfermedadesService.obtenerTodos();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> porId(@PathVariable Integer id) {
        try {
            EnfermedadesDTO dto = enfermedadesService.buscarPorId(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody Enfermedades enfermedades) {
        try {
            EnfermedadesDTO dto = enfermedadesService.guardar(enfermedades);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error en la transmisión de datos", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        String resultado = enfermedadesService.eliminar(id);
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }
}
