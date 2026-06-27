package com.veterniaria.ms_citas.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsumoDTO {

    private Integer id;
    private String nombre;
    private String descripcion;
    private Integer stock;
}
