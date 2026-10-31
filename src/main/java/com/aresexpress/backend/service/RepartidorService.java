package com.aresexpress.backend.service;

import com.aresexpress.backend.model.Repartidor;
import com.aresexpress.backend.repository.RepartidorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepartidorService {

    @Autowired
    private RepartidorRepository repartidorRepository;

    public List<Repartidor> obtenerTodos() {
        return repartidorRepository.findAll();
    }

    public Repartidor guardarRepartidor(Repartidor repartidor) {
        // Aseguramos valores por defecto para evitar errores de restricción NOT NULL en base de datos
        if (repartidor.getActivo() == null) {
            repartidor.setActivo(true);
        }
        if (repartidor.getCalificacionPromedio() == null) {
            repartidor.setCalificacionPromedio(5.0);
        }
        return repartidorRepository.save(repartidor);
    }

    public Repartidor obtenerPorId(Long id) {
        return repartidorRepository.findById(id).orElse(null);
    }

    public void eliminarRepartidor(Long id) {
        repartidorRepository.deleteById(id);
    }
}