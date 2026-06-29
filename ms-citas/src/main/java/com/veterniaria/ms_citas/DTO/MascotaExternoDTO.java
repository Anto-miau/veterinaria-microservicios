package com.veterniaria.ms_citas.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MascotaExternoDTO {

    private Integer id;
    private String nombre;
    private String color;
    private Integer edad;
    private String sexo;
}
