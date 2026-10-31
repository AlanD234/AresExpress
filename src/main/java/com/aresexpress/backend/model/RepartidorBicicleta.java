package com.aresexpress.backend.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("BICICLETA")
@Data
@EqualsAndHashCode(callSuper = true)

public class RepartidorBicicleta extends Repartidor{
    private boolean tieneCascoSeguridad;

    @Override
    public double calcularTarifaEnvio(double distanciaKm){
        return 5.0 + (distanciaKm * 2.0);
    }
}


