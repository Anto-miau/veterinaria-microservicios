package com.veterniaria.ms_citas.model;

import java.time.LocalDateTime;
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
@Table(name = "cita")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El motivo es obligatorio")
    @Size(min = 5, max = 300, message = "El motivo debe tener entre 5 y 300 caracteres")
    private String motivo;

    @NotNull(message = "La fecha y hora no pueden estar vacias")
    private LocalDateTime fechaHora;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    // Mascota vive en ms-mascotas, solo guardamos su ID
    @NotNull(message = "El id de la mascota es obligatorio")
    private Integer mascotaId;

    @OneToMany(mappedBy = "cita")
    private List<Pagos> pagos;

    @OneToMany(mappedBy = "cita")
    private List<Tratamientos> tratamientos;

    @OneToMany(mappedBy = "cita")
    private List<Veterinarios> veterinarios;
}
