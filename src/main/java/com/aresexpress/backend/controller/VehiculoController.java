package com.aresexpress.backend.controller;

import com.aresexpress.backend.dto.VehiculoDTO;
import com.aresexpress.backend.model.Repartidor;
import com.aresexpress.backend.model.Vehiculo;
import com.aresexpress.backend.repository.RepartidorRepository;
import com.aresexpress.backend.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehiculos")
@CrossOrigin(origins = "*")
public class VehiculoController {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private RepartidorRepository repartidorRepository;

    @GetMapping
    public List<Vehiculo> listarVehiculos() {
        return vehiculoRepository.findAll();
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarVehiculo(@RequestBody VehiculoDTO dto) {
        if (vehiculoRepository.existsByPlaca(dto.getPlaca())) {
            return ResponseEntity.badRequest().body(Map.of("error", "La placa ya se encuentra registrada en el sistema."));
        }

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(dto.getPlaca());
        vehiculo.setTipoVehiculo(dto.getTipoVehiculo());
        vehiculo.setMarca(dto.getMarca());
        vehiculo.setLinea(dto.getLinea());
        vehiculo.setModelo(dto.getModelo());
        vehiculo.setColor(dto.getColor());
        vehiculo.setCilindrada(dto.getCilindrada());
        vehiculo.setChasisVin(dto.getChasisVin());
        vehiculo.setCapacidadKg(dto.getCapacidadKg());
        vehiculo.setActivo(true);

        if (dto.getRepartidorId() != null) {
            Repartidor repartidor = repartidorRepository.findById(dto.getRepartidorId()).orElse(null);
            vehiculo.setRepartidor(repartidor);
        }

        vehiculoRepository.save(vehiculo);
        return ResponseEntity.ok(Map.of("message", "Vehículo registrado exitosamente."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarVehiculo(@PathVariable Long id, @RequestBody VehiculoDTO dto) {
        return vehiculoRepository.findById(id).map(vehiculo -> {
            if (!vehiculo.getPlaca().equals(dto.getPlaca()) && vehiculoRepository.existsByPlaca(dto.getPlaca())) {
                return ResponseEntity.badRequest().body(Map.of("error", "La placa ya se encuentra registrada en otro vehículo."));
            }

            vehiculo.setPlaca(dto.getPlaca());
            vehiculo.setTipoVehiculo(dto.getTipoVehiculo());
            vehiculo.setMarca(dto.getMarca());
            vehiculo.setLinea(dto.getLinea());
            vehiculo.setModelo(dto.getModelo());
            vehiculo.setColor(dto.getColor());
            vehiculo.setCilindrada(dto.getCilindrada());
            vehiculo.setChasisVin(dto.getChasisVin());
            vehiculo.setCapacidadKg(dto.getCapacidadKg());

            if (dto.getRepartidorId() != null) {
                Repartidor repartidor = repartidorRepository.findById(dto.getRepartidorId()).orElse(null);
                vehiculo.setRepartidor(repartidor);
            } else {
                vehiculo.setRepartidor(null);
            }

            vehiculoRepository.save(vehiculo);
            return ResponseEntity.ok(Map.of("message", "Vehículo actualizado correctamente."));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> darDeBajaVehiculo(@PathVariable Long id) {
        return vehiculoRepository.findById(id).map(vehiculo -> {
            vehiculo.setActivo(false);
            vehiculoRepository.save(vehiculo);
            return ResponseEntity.ok(Map.of("message", "Vehículo dado de baja correctamente."));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstadoVehiculo(@PathVariable Long id) {
        return vehiculoRepository.findById(id).map(vehiculo -> {
            vehiculo.setActivo(!vehiculo.getActivo());
            vehiculoRepository.save(vehiculo);
            return ResponseEntity.ok(Map.of("message", "Estado del vehículo actualizado correctamente."));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}