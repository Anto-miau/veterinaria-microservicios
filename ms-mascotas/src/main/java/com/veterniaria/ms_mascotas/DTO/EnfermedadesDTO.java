package com.veterniaria.ms_mascotas.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnfermedadesDTO {
    
    private Integer id;
    private Integer mascotaId;
    private String mascotaNombre;
    private Integer enfermedadId;
    private String enfermedadNombre;
}
