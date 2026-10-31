package com.aresexpress.backend.repository;

import com.aresexpress.backend.model.FacturaFel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacturaFelRepository extends JpaRepository<FacturaFel, Long> {
    Optional<FacturaFel> findByPedidoId(Long pedidoId);
}