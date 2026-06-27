package com.veterniaria.ms_citas.controller.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.veterniaria.ms_citas.DTO.TratamientoDTO;
import com.veterniaria.ms_citas.model.Tratamiento;
import com.veterniaria.ms_citas.service.TratamientoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/tratamientos")
public class TratamientoController {

    @Autowired
    private TratamientoService tratamientoService;

    @GetMapping
    public ResponseEntity<List<TratamientoDTO>> todas() {
        List<TratamientoDTO> lista = tratamientoService.obtenerTodos();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> porId(@PathVariable Integer id) {
        try {
            TratamientoDTO dto = tratamientoService.buscarPorId(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody Tratamiento tratamiento) {
        try {
            TratamientoDTO dto = tratamientoService.guardar(tratamiento);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error en la transmisión de datos", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Tratamiento tratamiento) {
        try {
            TratamientoDTO dto = tratamientoService.actualizar(id, tratamiento);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        String resultado = tratamientoService.eliminar(id);
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }
}
