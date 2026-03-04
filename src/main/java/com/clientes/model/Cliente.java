package com.clientes.model;

public class Cliente {

    private int id;
    private String nombre;
    private int edad;
    private String ciudad;
    private String tipoCliente;

    public Cliente(String nombre, int edad, String ciudad, String tipoCliente) {
        this.nombre = nombre;
        this.edad = edad;
        this.ciudad = ciudad;
        this.tipoCliente = tipoCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }
}   
