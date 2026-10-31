package com.aresexpress.backend.repository;

import com.aresexpress.backend.model.Comercio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// al extender jparepository heredamos métodos automáticos para guardar, borrar y buscar comercios
@Repository
public interface ComercioRepository extends JpaRepository<Comercio, Long> {
    
    // Método personalizado para filtrar los comercios según su estado (true = activos, false = vetados)
    List<Comercio> findByActivo(Boolean activo);
    
}