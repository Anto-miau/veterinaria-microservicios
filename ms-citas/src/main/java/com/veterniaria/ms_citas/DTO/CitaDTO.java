package com.veterniaria.ms_citas.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitaDTO {

    private MascotaExternoDTO mascota;
    private Integer id;
    private String motivo;
    private LocalDateTime fechaHora;
    private String estado;
    private Integer mascotaId;
}
