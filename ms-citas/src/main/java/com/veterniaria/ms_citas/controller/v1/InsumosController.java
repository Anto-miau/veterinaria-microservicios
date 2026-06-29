package com.veterniaria.ms_citas.controller.v1;

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

import com.veterniaria.ms_citas.DTO.InsumosDTO;
import com.veterniaria.ms_citas.model.Insumos;
import com.veterniaria.ms_citas.service.InsumosService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/insumos-tratamiento")
public class InsumosController {

    @Autowired
    private InsumosService insumosService;

    @GetMapping
    public ResponseEntity<List<InsumosDTO>> todas() {
        List<InsumosDTO> lista = insumosService.obtenerTodos();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> porId(@PathVariable Integer id) {
        try {
            InsumosDTO dto = insumosService.buscarPorId(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody Insumos insumos) {
        try {
            InsumosDTO dto = insumosService.guardar(insumos);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error en la transmisión de datos", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        String resultado = insumosService.eliminar(id);
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }
}
