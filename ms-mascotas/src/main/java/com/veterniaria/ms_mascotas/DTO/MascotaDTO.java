package com.veterniaria.ms_mascotas.DTO;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MascotaDTO {
    
    private Integer id;
    private String nombre;
    private String color;
    private Integer edad;
    private Date fechaNacimiento;
    private String sexo;
    private Long duenoId;
    private Integer razaId;
    private String razaNombre;
}
