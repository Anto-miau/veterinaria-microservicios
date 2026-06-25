package com.veterniaria.ms_mascotas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.veterniaria.ms_mascotas.model.Especie;

public interface EspecieRepository extends JpaRepository <Especie, Integer>{

}
