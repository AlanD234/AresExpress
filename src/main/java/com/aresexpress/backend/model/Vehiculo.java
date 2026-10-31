package com.aresexpress.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "vehiculo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String placa;

    @Column(nullable = false)
    private String tipoVehiculo; // AUTOMOVIL, MOTO, BICICLETA

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String linea;

    @Column(nullable = false)
    private Integer modelo;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private Integer cilindrada;

    @Column(nullable = false, unique = true)
    private String chasisVin;

    private Double capacidadKg;

    @Column(nullable = false)
    private Boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "repartidor_id", nullable = true)
    private Repartidor repartidor;
}