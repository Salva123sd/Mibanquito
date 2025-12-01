package com.ARFastCheck.ARFastCheck.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Persona {

    @Id
    private String dni;
    @Column(nullable=false)
    private String nombres;
    @Column(nullable=false)
    private String apellidos;

    //Constructor's
    public Persona() {}

    public Persona(String dni, String nombres, String apellidos) {
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
    }

    //Getters y Setters
    public String getDni() {return dni;}
    public void setDni(String dni) {this.dni = dni;}

    public String getNombres() {return nombres;}
    public void setNombres(String nombres) {this.nombres = nombres;}

    public String getApellidos() {return apellidos;}
    public void setApellidos(String apellidos) {this.apellidos = apellidos;}
}
