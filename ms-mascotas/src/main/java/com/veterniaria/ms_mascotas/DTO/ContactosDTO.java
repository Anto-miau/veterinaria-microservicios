package com.veterniaria.ms_mascotas.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactosDTO {

    private Integer id;
    private Integer mascotaId;
    private String mascotaNombre;
    private Integer contactoId;
    private String contactoNombre;
}
