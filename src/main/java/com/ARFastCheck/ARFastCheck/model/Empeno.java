
package com.ARFastCheck.ARFastCheck.model;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Empeno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Qué cosa dejó de garantía
    private String descripcion;

    // Valor estimado del objeto
    private BigDecimal valorEstimado;

    // Alguna nota extra
    private String observaciones;

    // 🔗 Relación con préstamo
    @ManyToOne
    @JoinColumn(name = "prestamo_id", nullable = false)
    private Prestamo prestamo;

    // ====== getters y setters ======

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getValorEstimado() { return valorEstimado; }
    public void setValorEstimado(BigDecimal valorEstimado) { this.valorEstimado = valorEstimado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public Prestamo getPrestamo() { return prestamo; }
    public void setPrestamo(Prestamo prestamo) { this.prestamo = prestamo; }
}
