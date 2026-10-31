package com.aresexpress.backend.repository;

import com.aresexpress.backend.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Solo trae pedidos que estén PENDIENTES y coincidan con el vehículo (si ya están EN_CAMINO o ENTREGADO, no se muestran)
    @Query("SELECT p FROM Pedido p WHERE p.estado = 'PENDIENTE' AND (p.tipoVehiculo = :tipoVehiculo OR p.tipoVehiculo IS NULL)")
    List<Pedido> findPedidosDisponiblesPorVehiculo(@Param("tipoVehiculo") String tipoVehiculo);

    List<Pedido> findByClienteId(Long clienteId);

    // Método para verificar si un repartidor ya tiene un pedido activo en camino
    boolean existsByRepartidorIdAndEstado(Long repartidorId, com.aresexpress.backend.model.EstadoPedido estado);
}