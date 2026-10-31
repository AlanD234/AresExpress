package com.aresexpress.backend.controller;

import com.aresexpress.backend.model.Cliente;
import com.aresexpress.backend.model.EstadoPedido;
import com.aresexpress.backend.model.Pedido;
import com.aresexpress.backend.repository.ClienteRepository;
import com.aresexpress.backend.repository.PedidoRepository;
import com.aresexpress.backend.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping
    public List<Pedido> obtenerTodos() {
        return pedidoService.obtenerTodos();
    }

    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody Pedido pedido) {
        try {
            Pedido nuevoPedido = pedidoService.guardarPedido(pedido);
            return ResponseEntity.ok(nuevoPedido);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    // Endpoint para filtrar los pedidos disponibles por tipo de vehículo
    @GetMapping("/disponibles/{tipoVehiculo}")
    public List<Pedido> listarPedidosDisponibles(@PathVariable String tipoVehiculo) {
        return pedidoRepository.findPedidosDisponiblesPorVehiculo(tipoVehiculo);
    }

    // Endpoint para listar los pedidos de un cliente específico por su ID
    @GetMapping("/cliente/{clienteId}")
    public List<Pedido> listarPedidosPorCliente(@PathVariable Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    // Endpoint para listar los pedidos filtrando directamente por el correo del cliente logueado
    @GetMapping("/cliente/correo/{correo}")
    public ResponseEntity<?> listarPedidosPorCorreoCliente(@PathVariable String correo) {
        Optional<Cliente> clienteOpt = clienteRepository.findByCorreo(correo);
        if (clienteOpt.isPresent()) {
            List<Pedido> pedidos = pedidoRepository.findByClienteId(clienteOpt.get().getId());
            return ResponseEntity.ok(pedidos);
        }
        return ResponseEntity.status(404).body(Collections.singletonMap("error", "Cliente no encontrado para el correo: " + correo));
    }

    @PutMapping("/{pedidoId}/asignar/{repartidorId}")
    public ResponseEntity<?> asignarPedido(@PathVariable Long pedidoId, @PathVariable Long repartidorId) {
        try {
            Pedido pedidoActualizado = pedidoService.asignarPedido(pedidoId, repartidorId);
            return ResponseEntity.ok(pedidoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    // Nuevo: Endpoint para actualizar el estado del pedido (ej. EN_CAMINO, ENTREGADO)
    @PutMapping("/{pedidoId}/estado")
    public ResponseEntity<?> actualizarEstadoPedido(@PathVariable Long pedidoId, @RequestParam String estado) {
        try {
            Pedido pedido = pedidoRepository.findById(pedidoId)
                    .orElseThrow(() -> new RuntimeException("Pedido no encontrado."));

            pedido.setEstado(EstadoPedido.valueOf(estado.toUpperCase()));
            Pedido pedidoActualizado = pedidoRepository.save(pedido);

            return ResponseEntity.ok(pedidoActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}