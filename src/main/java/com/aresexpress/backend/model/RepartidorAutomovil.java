package com.aresexpress.backend.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("AUTOMOVIL")
@Data
@EqualsAndHashCode(callSuper = true)

public class RepartidorAutomovil extends Repartidor{
    private String numeroLicencia;


@Override
public double calcularTarifaEnvio(double distanciaKm){
    return 20.0 + (distanciaKm  * 5.0);
}
}