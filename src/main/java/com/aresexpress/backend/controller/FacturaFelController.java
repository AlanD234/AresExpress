package com.aresexpress.backend.controller;

import com.aresexpress.backend.model.FacturaFel;
import com.aresexpress.backend.repository.FacturaFelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/facturas")
@CrossOrigin(origins = "*")
public class FacturaFelController {

    @Autowired
    private FacturaFelRepository facturaRepository;

    @PostMapping
    public ResponseEntity<?> guardarFactura(@RequestBody FacturaFel factura) {
        try {
            // Si no trae fecha asignada, le ponemos el momento actual
            if (factura.getFechaEmision() == null) {
                factura.setFechaEmision(LocalDateTime.now());
            }
            
            // Verificamos si ya existe una factura para este pedido para evitar duplicados exactos
            if (factura.getPedidoId() != null && facturaRepository.findByPedidoId(factura.getPedidoId()).isPresent()) {
                return ResponseEntity.ok("La factura para este pedido ya se encontraba registrada.");
            }

            facturaRepository.save(factura);
            return ResponseEntity.ok("Factura FEL registrada y guardada exitosamente en base de datos.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al guardar la factura: " + e.getMessage());
        }
    }
}