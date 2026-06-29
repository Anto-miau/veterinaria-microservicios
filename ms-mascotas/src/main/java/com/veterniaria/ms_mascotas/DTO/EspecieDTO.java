package com.veterniaria.ms_mascotas.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EspecieDTO {
    
    private Integer id;
    private String nombre;
}
