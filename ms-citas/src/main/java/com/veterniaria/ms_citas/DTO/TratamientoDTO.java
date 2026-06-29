package com.veterniaria.ms_citas.DTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TratamientoDTO {

    private Integer id;
    private String diagnostico;
    private String descripcion;
    private Date fechaInicio;
    private Date fechaTermino;
}
