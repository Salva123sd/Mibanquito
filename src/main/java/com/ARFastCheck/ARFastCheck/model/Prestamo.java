package com.ARFastCheck.ARFastCheck.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "dni_persona", nullable = false)
    private Persona persona;

    @ManyToOne
    @JoinColumn(name = "id_objeto", nullable = false)
    private Objeto objeto;

    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private LocalDate fechaLimite;
    private String estado;

    // 🔗 NUEVO: relación con empeños
    @OneToMany(mappedBy = "prestamo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Empeno> empenos = new ArrayList<>();

    // 🔗 NUEVO: relación con documentos
    @OneToMany(mappedBy = "prestamo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentoPrestamo> documentos = new ArrayList<>();

    public Prestamo() {}

    // ===== getters y setters =====

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Persona getPersona() { return persona; }
    public void setPersona(Persona persona) { this.persona = persona; }

    public Objeto getObjeto() { return objeto; }
    public void setObjeto(Objeto objeto) { this.objeto = objeto; }

    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(LocalDate fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }

    public LocalDate getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDate fechaLimite) { this.fechaLimite = fechaLimite; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<Empeno> getEmpenos() { return empenos; }
    public void setEmpenos(List<Empeno> empenos) { this.empenos = empenos; }

    public List<DocumentoPrestamo> getDocumentos() { return documentos; }
    public void setDocumentos(List<DocumentoPrestamo> documentos) { this.documentos = documentos; }
}
