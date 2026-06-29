package com.veterniaria.ms_mascotas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veterniaria.ms_mascotas.model.Mascota;

public interface MascotaRepository extends JpaRepository <Mascota, Integer>{

}
