package com.ARFastCheck.ARFastCheck.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class DocumentoPrestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ej: "DNI", "Contrato", "Pagaré"
    private String tipoDocumento;

    // Ej: número de DNI, código de contrato, etc.
    private String numeroDocumento;

    // Si luego guardas archivos físicos (pdf, imagen, etc.)
    private String rutaArchivo;

    private LocalDate fechaRegistro = LocalDate.now();

    // 🔗 Relación con préstamo
    @ManyToOne
    @JoinColumn(name = "prestamo_id", nullable = false)
    private Prestamo prestamo;

    // ====== getters y setters ======

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }

    public String getRutaArchivo() { return rutaArchivo; }
    public void setRutaArchivo(String rutaArchivo) { this.rutaArchivo = rutaArchivo; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public Prestamo getPrestamo() { return prestamo; }
    public void setPrestamo(Prestamo prestamo) { this.prestamo = prestamo; }
}
