package com.aresexpress.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado;

    @Column(nullable = false)
    private Double total;

    // Nuevos campos para costo de envío y tipo de vehículo seleccionado
    @Column(name = "costo_envio")
    private Double costoEnvio;

    @Column(name = "tipo_vehiculo")
    private String tipoVehiculo;

    // Campos fiscales/facturación por pedido
    @Column(name = "nit_cliente")
    private String nitCliente;

    @Column(name = "nombre_factura")
    private String nombreFactura;

    @Column(name = "nit_comercio")
    private String nitComercio;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "comercio_id", nullable = false)
    private Comercio comercio;

    @ManyToOne
    @JoinColumn(name = "repartidor_id")
    private Repartidor repartidor;
}