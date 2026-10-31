package com.aresexpress.backend.repository;

import com.aresexpress.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar un usuario por su correo electrónico (indispensable para el Login)
    Optional<Usuario> findByCorreo(String correo);

    // Listar usuarios por su rol (por ejemplo, para filtrar administradores, clientes o repartidores)
    List<Usuario> findByRol(String rol);
}