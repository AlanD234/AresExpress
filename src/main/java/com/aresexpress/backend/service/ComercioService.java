package com.aresexpress.backend.service;

import com.aresexpress.backend.model.Comercio;
import com.aresexpress.backend.repository.ComercioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComercioService {

    @Autowired
    private ComercioRepository comercioRepository;

    // Guardar o actualizar un comercio asegurando que nazca activo si no se define
    public Comercio guardarComercio(Comercio comercio) {
        if (comercio.getActivo() == null) {
            comercio.setActivo(true); // Por defecto nace activo
        }
        return comercioRepository.save(comercio);
    }

    // Listar solo los comercios activos (para el panel principal)
    public List<Comercio> obtenerActivos() {
        return comercioRepository.findByActivo(true);
    }

    // Listar solo los comercios vetados (para el historial de veto)
    public List<Comercio> obtenerVetados() {
        return comercioRepository.findByActivo(false);
    }

    // Buscar un comercio específico por su ID (necesario para las actualizaciones y cambios de estado)
    public Comercio obtenerComercioPorId(Long id) {
        return comercioRepository.findById(id).orElse(null);
    }

    // Cambiar estado (Dar de baja lógica o Restaurar)
    public void cambiarEstadoComercio(Long id, Boolean estado) {
        Comercio comercio = obtenerComercioPorId(id);
        if (comercio != null) {
            comercio.setActivo(estado);
            comercioRepository.save(comercio);
        }
    }
}