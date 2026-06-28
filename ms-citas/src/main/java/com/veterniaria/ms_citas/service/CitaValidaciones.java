package com.veterniaria.ms_citas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.veterniaria.ms_citas.DTO.CitaDTO;
import com.veterniaria.ms_citas.DTO.MascotaExternoDTO;
import com.veterniaria.ms_citas.model.Cita;

import reactor.core.publisher.Mono;

@Service
public class CitaValidaciones {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Boolean validarNullVacio(Cita cita) {
        if (cita.getMotivo() == null || cita.getMotivo().trim().length() == 0) {
            return false;
        }
        if (cita.getFechaHora() == null) {
            return false;
        }
        if (cita.getEstado() == null || cita.getEstado().trim().length() == 0) {
            return false;
        }
        if (cita.getMascotaId() == null) {
            return false;
        }
        return true;
    }

    public MascotaExternoDTO obtenerMascota(Integer id) {
        MascotaExternoDTO mascotaRecuperada = new MascotaExternoDTO();
        try {
            MascotaExternoDTO resultado = webClientBuilder.build()
                .get()
                .uri("http://ms-mascotas/api/v1/mascotas/" + id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                .bodyToMono(MascotaExternoDTO.class)
                .block();
            if (resultado != null) {
                return resultado;
            }
            mascotaRecuperada.setId(0);
            mascotaRecuperada.setNombre("mascota no encontrada");
            return mascotaRecuperada;
        } catch (Exception e) {
            mascotaRecuperada.setId(0);
            mascotaRecuperada.setNombre("no se pudo conectar con el servicio de mascotas");
            return mascotaRecuperada;
        }
    }

    public CitaDTO convertirADTO(Cita cita) {
        CitaDTO dto = new CitaDTO();
        dto.setId(cita.getId());
        dto.setMotivo(cita.getMotivo());
        dto.setFechaHora(cita.getFechaHora());
        dto.setEstado(cita.getEstado());
        dto.setMascotaId(cita.getMascotaId());
        dto.setMascota(obtenerMascota(cita.getMascotaId()));
        return dto;
    }
}
