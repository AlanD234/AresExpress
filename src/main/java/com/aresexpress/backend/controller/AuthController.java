package com.aresexpress.backend.controller;

import com.aresexpress.backend.model.Cliente;
import com.aresexpress.backend.model.Usuario;
import com.aresexpress.backend.repository.ClienteRepository;
import com.aresexpress.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Map<String, Object> payload) {
        try {
            String correo = (String) payload.get("correo");
            String password = (String) payload.get("password");
            String nombre = (String) payload.get("nombre");
            String telefono = (String) payload.getOrDefault("telefono", "00000000");
            String direccion = (String) payload.getOrDefault("direccion", "Amatitlán");
            String nit = (String) payload.getOrDefault("nit", "CF");
            String rolSolicitado = (String) payload.getOrDefault("rol", "cliente");

            if (correo == null || correo.trim().isEmpty() || password == null || password.trim().isEmpty() || nombre == null || nombre.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Todos los campos principales son obligatorios."));
            }

            if (usuarioRepository.findByCorreo(correo.trim()).isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Este correo electrónico ya se encuentra registrado."));
            }

            // SEGURIDAD: Nadie puede registrarse como admin por esta vía pública
            if ("admin".equals(rolSolicitado)) {
                rolSolicitado = "cliente";
            }

            // 1. Crear el objeto Usuario
            Usuario usuario = new Usuario();
            usuario.setNombre(nombre.trim());
            usuario.setCorreo(correo.trim());
            usuario.setPassword(password.trim());
            usuario.setRol(rolSolicitado);

            // REPARTIDOR PENDIENTE DE APROBACIÓN: Si elige repartidor, nace inactivo (activo = false)
            if ("repartidor".equals(rolSolicitado)) {
                usuario.setActivo(false);
                usuarioRepository.save(usuario);
                return ResponseEntity.ok(Map.of("message", "Solicitud de repartidor enviada con éxito. Pendiente de aprobación."));
            } else {
                // Si es cliente, nace activo y se vincula con la tabla cliente
                usuario.setActivo(true);
                usuarioRepository.save(usuario);

                Cliente cliente = new Cliente();
                cliente.setNombre(nombre.trim());
                cliente.setCorreo(correo.trim());
                cliente.setTelefono(telefono);
                cliente.setDireccionEntrega(direccion);
                cliente.setNit(nit);
                cliente.setUsuario(usuario);
                clienteRepository.save(cliente);

                return ResponseEntity.ok(Map.of("message", "Usuario registrado exitosamente como cliente."));
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "No se pudo completar el registro: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String correo = credentials.get("correo");
            String password = credentials.get("password");

            if (correo == null || password == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Correo y contraseña son obligatorios."));
            }

            Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo.trim());

            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(401).body(Map.of("error", "Correo o contraseña incorrectos."));
            }

            Usuario usuario = usuarioOpt.get();

            if (!usuario.getPassword().equals(password)) {
                return ResponseEntity.status(401).body(Map.of("error", "Correo o contraseña incorrectos."));
            }

            // VALIDACIÓN DE APROBACIÓN: Si es un repartidor pendiente, bloquear acceso
            if (!usuario.isActivo()) {
                return ResponseEntity.status(403).body(Map.of("error", "Tu cuenta de repartidor aún está pendiente de aprobación por un administrador."));
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Login exitoso",
                    "correo", usuario.getCorreo(),
                    "nombre", usuario.getNombre(),
                    "rol", usuario.getRol()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error en el servidor: " + e.getMessage()));
        }
    }

    // 1. Endpoint para listar todos los repartidores pendientes de aprobación (activo = false)
    @GetMapping("/admin/repartidores/pendientes")
    public ResponseEntity<?> listarRepartidoresPendientes() {
        try {
            List<Usuario> pendientes = usuarioRepository.findAll().stream()
                    .filter(u -> "repartidor".equals(u.getRol()) && !u.isActivo())
                    .toList();

            return ResponseEntity.ok(pendientes);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error al listar pendientes: " + e.getMessage()));
        }
    }

    // 2. Endpoint para aprobar a un repartidor (cambiar activo a true)
    @PutMapping("/admin/repartidores/aprobar/{id}")
    public ResponseEntity<?> aprobarRepartidor(@PathVariable Long id) {
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);

            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado."));
            }

            Usuario usuario = usuarioOpt.get();

            if (!"repartidor".equals(usuario.getRol())) {
                return ResponseEntity.badRequest().body(Map.of("error", "El usuario seleccionado no es un repartidor."));
            }

            usuario.setActivo(true);
            usuarioRepository.save(usuario);

            return ResponseEntity.ok(Map.of("message", "Repartidor aprobado exitosamente. Ya puede iniciar sesión."));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error al aprobar repartidor: " + e.getMessage()));
        }
    }
}