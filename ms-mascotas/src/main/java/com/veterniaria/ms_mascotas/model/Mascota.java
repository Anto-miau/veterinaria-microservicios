package com.veterniaria.ms_mascotas.model;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mascota")
public class Mascota {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El color es obligatorio")
    @Size(min = 2, max = 50, message = "El color debe tener entre 2 y 50 caracteres")
    private String color;

    @Min(value = 0, message = "La edad no puede ser negativa")
    private Integer edad;

    private Date fechaNacimiento;

    @NotBlank(message = "El sexo es obligatorio")
    private String sexo;

    // Dueno vive en ms-personas, solo guardamos su ID
    @NotNull(message = "El id del dueño es obligatorio")
    private Long duenoId;

    //relaciones------------------------------------------
    @ManyToOne
    @JoinColumn(name = "raza_id", nullable = false)
    private Raza raza;

    // Citas vive en ms-citas, no va acá

    //tablas intermedias----------------------------------
    @OneToMany(mappedBy = "mascota")
    private List<Contactos> contactos;

    @OneToMany(mappedBy = "mascota")
    private List<Enfermedades> enfermedades;
}