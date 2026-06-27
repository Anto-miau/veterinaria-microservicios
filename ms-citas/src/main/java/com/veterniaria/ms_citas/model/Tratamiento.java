package com.veterniaria.ms_citas.model;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tratamiento")
public class Tratamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El diagnóstico es obligatorio")
    @Size(min = 5, max = 300, message = "El diagnóstico debe tener entre 5 y 300 caracteres")
    private String diagnostico;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 5, max = 300, message = "La descripción debe tener entre 5 y 300 caracteres")
    private String descripcion;

    @NotNull(message = "La fecha no puede estar vacia")
    private Date fechaInicio;

    @NotNull(message = "La fecha no puede estar vacia")
    private Date fechaTermino;

    @OneToMany(mappedBy = "tratamiento")
    private List<Insumos> insumos;

    @OneToMany(mappedBy = "tratamiento")
    private List<Tratamientos> citas;
}
