package com.aresexpress.backend.service;

import com.aresexpress.backend.exception.RecursoNoEncontradoException;
import com.aresexpress.backend.model.Cliente;
import com.aresexpress.backend.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> obtenerTodos() {
        return clienteRepository.findAll();
    }

    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + id));
    }

    public Cliente obtenerPorCorreo(String correo) {
        return clienteRepository.findByCorreo(correo).orElse(null);
    }

    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente cambiarEstadoCliente(Long id, Boolean activo) {
        Cliente cliente = clienteRepository.findById(id).orElse(null);
        if (cliente != null) {
            cliente.setActivo(activo);
            return clienteRepository.save(cliente);
        }
        return null;
    }
}