package com.aresexpress.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "facturas_fel")
public class FacturaFel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long pedidoId;
    private String emisorNombre;
    private String emisorNit;
    private String clienteNombre;
    private String clienteNit;
    private String descripcionProducto;
    private Double subtotal;
    private Double costoEnvio;
    private Double total;
    private String tipoVehiculo;
    private LocalDateTime fechaEmision;

    // Constructor vacío
    public FacturaFel() {
    }

    // Constructor con campos
    public FacturaFel(Long pedidoId, String emisorNombre, String emisorNit, String clienteNombre, 
                      String clienteNit, String descripcionProducto, Double subtotal, 
                      Double costoEnvio, Double total, String tipoVehiculo, LocalDateTime fechaEmision) {
        this.pedidoId = pedidoId;
        this.emisorNombre = emisorNombre;
        this.emisorNit = emisorNit;
        this.clienteNombre = clienteNombre;
        this.clienteNit = clienteNit;
        this.descripcionProducto = descripcionProducto;
        this.subtotal = subtotal;
        this.costoEnvio = costoEnvio;
        this.total = total;
        this.tipoVehiculo = tipoVehiculo;
        this.fechaEmision = fechaEmision;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }

    public String getEmisorNombre() { return emisorNombre; }
    public void setEmisorNombre(String emisorNombre) { this.emisorNombre = emisorNombre; }

    public String getEmisorNit() { return emisorNit; }
    public void setEmisorNit(String emisorNit) { this.emisorNit = emisorNit; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public String getClienteNit() { return clienteNit; }
    public void setClienteNit(String clienteNit) { this.clienteNit = clienteNit; }

    public String getDescripcionProducto() { return descripcionProducto; }
    public void setDescripcionProducto(String descripcionProducto) { this.descripcionProducto = descripcionProducto; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Double getCostoEnvio() { return costoEnvio; }
    public void setCostoEnvio(Double costoEnvio) { this.costoEnvio = costoEnvio; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public String getTipoVehiculo() { return tipoVehiculo; }
    public void setTipoVehiculo(String tipoVehiculo) { this.tipoVehiculo = tipoVehiculo; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }
}