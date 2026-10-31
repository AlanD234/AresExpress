package com.aresexpress.backend.dto;

import lombok.Data;

@Data
public class VehiculoDTO {
    private String placa;
    private String tipoVehiculo;
    private String marca;
    private String linea;
    private Integer modelo;
    private String color;
    private Integer cilindrada;
    private String chasisVin;
    private Double capacidadKg;
    private Long repartidorId;
}