package com.aresexpress.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RepartidorMoto.class, name = "MOTO"),
        @JsonSubTypes.Type(value = RepartidorAutomovil.class, name = "AUTOMOVIL"),
        @JsonSubTypes.Type(value = RepartidorBicicleta.class, name = "BICICLETA")
})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_repartidor", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Repartidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación de uno a uno con la tabla usuario centralizada
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private Usuario usuario;

    private String dpi;          // Documento de identificación
    private String licencia;     // Número de licencia
    private Double calificacionPromedio;

    // Campo activo obligatorio para evitar errores NOT NULL en la base de datos
    @Column(nullable = false)
    private Boolean activo = true;

    // Método abstracto para calcular la tarifa según el tipo de vehículo
    public abstract double calcularTarifaEnvio(double distanciaKm);
}