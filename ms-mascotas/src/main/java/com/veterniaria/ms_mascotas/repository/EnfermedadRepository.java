package com.veterniaria.ms_mascotas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veterniaria.ms_mascotas.model.Enfermedad;

public interface EnfermedadRepository extends JpaRepository <Enfermedad, Integer>{

}
