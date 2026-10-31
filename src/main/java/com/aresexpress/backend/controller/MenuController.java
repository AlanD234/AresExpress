package com.aresexpress.backend.controller;

import com.aresexpress.backend.model.Menu;
import com.aresexpress.backend.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/menus")
@CrossOrigin(origins = "*")
public class MenuController {

    @Autowired
    private MenuRepository menuRepository;

    private static final String UPLOAD_DIR = "uploads/menus/";

    @GetMapping("/comercio/{comercioId}")
    public List<Menu> obtenerMenusPorComercio(@PathVariable Long comercioId) {
        return menuRepository.findByComercioId(comercioId);
    }

    // Endpoint para CREAR un nuevo platillo (POST)
    @PostMapping
    public ResponseEntity<?> guardarMenu(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") String precioStr,
            @RequestParam("comercioId") String comercioIdStr,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen) {
        
        try {
            Double precio = Double.parseDouble(precioStr);
            Long comercioId = Long.parseLong(comercioIdStr);
            
            String imagenUrl = null;

            if (imagen != null && !imagen.isEmpty()) {
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                String fileName = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR + fileName);
                Files.write(filePath, imagen.getBytes());
                imagenUrl = "/api/menus/images/" + fileName;
            }

            Menu nuevoMenu = new Menu(nombre, descripcion, precio, imagenUrl, comercioId);
            menuRepository.save(nuevoMenu);

            return ResponseEntity.ok("Platillo guardado exitosamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error en el servidor al registrar el platillo: " + e.getMessage());
        }
    }

    // Endpoint para ACTUALIZAR un platillo existente (PUT) - NUEVO
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMenu(
            @PathVariable Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") String precioStr,
            @RequestParam("comercioId") String comercioIdStr,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen) {
        
        try {
            if (!menuRepository.existsById(id)) {
                return ResponseEntity.status(404).body("El platillo no existe");
            }

            Menu menuExistente = menuRepository.findById(id).get();
            Double precio = Double.parseDouble(precioStr);
            Long comercioId = Long.parseLong(comercioIdStr);
            
            menuExistente.setNombre(nombre);
            menuExistente.setDescripcion(descripcion);
            menuExistente.setPrecio(precio);
            menuExistente.setComercioId(comercioId);

            // Si se seleccionó una nueva imagen, se reemplaza
            if (imagen != null && !imagen.isEmpty()) {
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                String fileName = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR + fileName);
                Files.write(filePath, imagen.getBytes());
                menuExistente.setImagenUrl("/api/menus/images/" + fileName);
            }

            menuRepository.save(menuExistente);
            return ResponseEntity.ok("Platillo actualizado exitosamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error en el servidor al actualizar el platillo: " + e.getMessage());
        }
    }

    // Endpoint para ELIMINAR un platillo por su ID (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMenu(@PathVariable Long id) {
        try {
            if (menuRepository.existsById(id)) {
                menuRepository.deleteById(id);
                return ResponseEntity.ok("Platillo eliminado exitosamente");
            } else {
                return ResponseEntity.status(404).body("El platillo no existe");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al eliminar el platillo: " + e.getMessage());
        }
    }
}