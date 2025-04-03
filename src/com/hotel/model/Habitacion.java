/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.model;

/**
 *
 * @author pined
 */

import java.math.BigDecimal;

public class Habitacion {
    private int idHabitacion;
    private String tipo;
    private BigDecimal precio;
    private int capacidad;
    private boolean disponible;
    
    // Constructor vacío
    public Habitacion() {
    }
    
    // Constructor completo
    public Habitacion(int idHabitacion, String tipo, BigDecimal precio, int capacidad, boolean disponible) {
        this.idHabitacion = idHabitacion;
        this.tipo = tipo;
        this.precio = precio;
        this.capacidad = capacidad;
        this.disponible = disponible;
    }
    
    // Getters y Setters
    public int getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(int idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
    public String getEstado() {
        return disponible ? "Disponible" : "No Disponible";
    }
    
    public void setEstado(String estado) {
        this.disponible = estado.equalsIgnoreCase("Disponible");
    }
    
    @Override
    public String toString() {
        return "Habitación " + idHabitacion + " - " + tipo + " ($" + precio + ")";
    }
}