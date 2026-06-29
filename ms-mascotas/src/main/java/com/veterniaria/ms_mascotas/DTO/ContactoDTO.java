package com.veterniaria.ms_mascotas.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactoDTO {

    private Integer id;
    private String nombre;
    private String telefono;
    private String correo;
}
