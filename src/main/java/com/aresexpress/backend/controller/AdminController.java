package com.aresexpress.backend.controller;

import com.aresexpress.backend.model.Usuario;
import com.aresexpress.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 1. Registrar SubAdministrador
    @PostMapping("/registrar-subadmin")
    public ResponseEntity<?> registrarSubAdmin(
            @RequestParam String correoAdminActual,
            @RequestBody Usuario nuevoSubAdmin) {

        nuevoSubAdmin.setRol("subadmin");
        nuevoSubAdmin.setActivo(true);
        usuarioRepository.save(nuevoSubAdmin);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "SubAdministrador registrado correctamente."
        ));
    }

    // 2. Listar todos los SubAdministradores (Requerido por Editar y Vetar)
    @GetMapping("/subadmins")
    public ResponseEntity<List<Usuario>> listarSubAdmins(@RequestParam String correoAdminActual) {
        List<Usuario> subadmins = usuarioRepository.findByRol("subadmin");
        return ResponseEntity.ok(subadmins);
    }

    // 3. Actualizar SubAdministrador
    @PutMapping("/subadmin/{id}")
    public ResponseEntity<?> actualizarSubAdmin(
            @PathVariable Long id,
            @RequestParam String correoAdminActual,
            @RequestBody Usuario datosActualizados) {

        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "SubAdministrador no encontrado."
            ));
        }

        Usuario usuario = optionalUsuario.get();
        usuario.setNombre(datosActualizados.getNombre());
        usuario.setCorreo(datosActualizados.getCorreo());

        // Actualizar contraseña solo si se proporciona una nueva
        if (datosActualizados.getPassword() != null && !datosActualizados.getPassword().isBlank()) {
            usuario.setPassword(datosActualizados.getPassword());
        }

        usuarioRepository.save(usuario);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "SubAdministrador actualizado correctamente."
        ));
    }

    // 4. Dar de baja (Eliminar) SubAdministrador
    @DeleteMapping("/subadmin/{id}")
    public ResponseEntity<?> vetarSubAdmin(
            @PathVariable Long id,
            @RequestParam String correoAdminActual) {

        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "SubAdministrador no encontrado."
            ));
        }

        usuarioRepository.deleteById(id);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "SubAdministrador dado de baja correctamente."
        ));
    }
}