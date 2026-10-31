package com.aresexpress.backend.controller;

import com.aresexpress.backend.model.Cliente;
import com.aresexpress.backend.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<Cliente> listarClientes() {
        return clienteService.obtenerTodos();
    }

    // Endpoint por query param
    @GetMapping("/buscar")
    public ResponseEntity<Cliente> buscarPorCorreo(@RequestParam String correo) {
        Cliente cliente = clienteService.obtenerPorCorreo(correo);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint directo por path variable para el frontend
    @GetMapping("/correo/{correo}")
    public ResponseEntity<Cliente> obtenerPorCorreoPath(@PathVariable String correo) {
        Cliente cliente = clienteService.obtenerPorCorreo(correo);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public Cliente crearCliente(@RequestBody Cliente cliente) {
        return clienteService.guardarCliente(cliente);
    }

    @PutMapping("/{id}/estado")
    public Cliente cambiarEstadoCliente(@PathVariable Long id, @RequestParam Boolean activo) {
        return clienteService.cambiarEstadoCliente(id, activo);
    }
}