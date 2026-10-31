package com.aresexpress.backend.service;

import com.aresexpress.backend.model.EstadoPedido;
import com.aresexpress.backend.model.Pedido;
import com.aresexpress.backend.model.Repartidor;
import com.aresexpress.backend.repository.PedidoRepository;
import com.aresexpress.backend.repository.RepartidorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private RepartidorRepository repartidorRepository;

    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido guardarPedido(Pedido pedido) {
        if (pedido.getEstado() == null) {
            pedido.setEstado(EstadoPedido.PENDIENTE);
        }
        return pedidoRepository.save(pedido);
    }

    public Pedido asignarPedido(Long pedidoId, Long repartidorId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado."));

        // Validar que el pedido esté disponible (PENDIENTE) y no haya sido tomado por otro
        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new RuntimeException("Este pedido ya fue tomado por otro repartidor o ya no está disponible.");
        }

        Repartidor repartidor = repartidorRepository.findById(repartidorId)
                .orElseThrow(() -> new RuntimeException("Repartidor no encontrado."));

        // REGLA NUEVA: Verificar si el repartidor ya tiene un pedido EN_CAMINO
        boolean tienePedidoActivo = pedidoRepository.existsByRepartidorIdAndEstado(repartidorId, EstadoPedido.EN_CAMINO);
        if (tienePedidoActivo) {
            throw new RuntimeException("Ya tienes una entrega activa en curso. Debes finalizarla antes de aceptar otro pedido.");
        }

        pedido.setRepartidor(repartidor);
        pedido.setEstado(EstadoPedido.EN_CAMINO);

        return pedidoRepository.save(pedido);
    }
}