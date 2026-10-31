package com.aresexpress.backend.controller;

import com.aresexpress.backend.model.Repartidor;
import com.aresexpress.backend.repository.RepartidorRepository;
import com.aresexpress.backend.service.RepartidorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/repartidores")
@CrossOrigin(origins = "*")
public class RepartidorController {

    @Autowired
    private RepartidorService repartidorService;

    @Autowired
    private RepartidorRepository repartidorRepository;

    @GetMapping
    public List<Repartidor> listarRepartidores(){
        return repartidorService.obtenerTodos();
    }

    @PostMapping
    public Repartidor crearRepartidor(@RequestBody Repartidor repartidor){
        return repartidorService.guardarRepartidor(repartidor);
    }

    @DeleteMapping("/{id}")
    public void eliminarRepartidor(@PathVariable Long id) {
        repartidorService.eliminarRepartidor(id);
    }

    @PutMapping("/{id}/estado")
    public Repartidor cambiarEstadoRepartidor(@PathVariable Long id, @RequestParam Boolean activo) {
        Repartidor repartidor = repartidorService.obtenerPorId(id);
        if (repartidor != null && repartidor.getUsuario() != null) {
            repartidor.getUsuario().setActivo(activo);
            return repartidorService.guardarRepartidor(repartidor);
        }
        return null;
    }

    // Endpoint optimizado para buscar el repartidor directamente por el correo de su usuario asociado
    @GetMapping("/correo/{correo}")
    public ResponseEntity<?> obtenerRepartidorPorCorreo(@PathVariable String correo) {
        Optional<Repartidor> repartidorOpt = repartidorRepository.findByUsuarioCorreo(correo);

        if (repartidorOpt.isPresent()) {
            return ResponseEntity.ok(repartidorOpt.get());
        }

        return ResponseEntity.status(404).body(Map.of("error", "No se pudo encontrar el perfil de repartidor para el correo: " + correo));
    }
}