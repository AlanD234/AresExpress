package com.aresexpress.backend.repository;

import com.aresexpress.backend.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByComercioId(Long comercioId);
}