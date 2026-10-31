package com.aresexpress.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime; // Necesario para manejar las horas de apertura y cierre

// @Entity le indica a Hibernate que esta clase se convertirá en una tabla en Postgres
@Entity
@Table(name = "comercio")
// Lombok genera automáticamente getters, setters, equals y toString
@Data
@NoArgsConstructor // Genera un constructor vacío necesario para JPA
@AllArgsConstructor  // Genera un constructor con todos los atributos
public class Comercio {

    // Define la llave primaria de la tabla
    @Id
    // @GeneratedValue configura el autoincremento en la base de datos
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private Boolean abierto;

    // --- DATOS FISCALES Y DE FACTURACIÓN ---
    @Column(name = "razon_social")
    private String razonSocial;

    @Column(name = "nit")
    private String nit; // Agregado y respaldado con su anotación de columna

    @Column(name = "regimen_isr")
    private String regimenIsr;

    @Column(name = "agente_retenedor_iva")
    private Boolean agenteRetenedorIva;

    // --- CONTACTO Y LOGÍSTICA ---
    @Column(name = "contacto_persona")
    private String contactoPersona;

    private String telefono;

    // --- HORARIOS DE ATENCIÓN ---
    @Column(name = "dias_operacion")
    private String diasOperacion;

    @Column(name = "hora_apertura")
    private LocalTime horaApertura;

    @Column(name = "hora_cierre")
    private LocalTime horaCierre;
    
    // --- LOGOTIPO ---
    @Column(name = "logo_url")
    private String logoUrl;
    
    // --- ESTADO PARA VETO / BORRADO LÓGICO (TOGGLE) ---
    @Column(nullable = false)
    private Boolean activo = true;
    
}