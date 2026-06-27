package com.veterniaria.ms_citas.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TratamientosDTO {

    private Integer id;
    private Integer citaId;
    private String citaMotivo;
    private Integer tratamientoId;
    private String tratamientoDiagnostico;
}
