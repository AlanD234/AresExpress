package com.aresexpress.backend.repository;

import com.aresexpress.backend.model.Repartidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepartidorRepository extends JpaRepository<Repartidor, Long> {

    @Query("SELECT r FROM Repartidor r WHERE r.usuario.correo = :correo")
    Optional<Repartidor> findByUsuarioCorreo(@Param("correo") String correo);
}