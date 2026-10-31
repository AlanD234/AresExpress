package com.aresexpress.backend.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("MOTO")
@Data
@EqualsAndHashCode(callSuper = true)

public class RepartidorMoto extends Repartidor{
    private String placaMoto;

    @Override
    public double calcularTarifaEnvio(double distanciaKm){
        return 10.0 + (distanciaKm * 3.0);
    }

}