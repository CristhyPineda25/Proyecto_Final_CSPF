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
import java.util.Date;

public class Reserva {
    private int idReserva;
    private int idHuesped;
    private int idHabitacion;
    private Date fechaInicio;
    private Date fechaFin;
    private String estado;
    
    // Campos adicionales para mostrar información relacionada
    private String nombreHuesped;
    private String tipoHabitacion;
    
    // Constructor vacío
    public Reserva() {
    }
    
    // Constructor completo
    public Reserva(int idReserva, int idHuesped, int idHabitacion, Date fechaInicio, Date fechaFin, String estado) {
        this.idReserva = idReserva;
        this.idHuesped = idHuesped;
        this.idHabitacion = idHabitacion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }
    
    // Getters y Setters
    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public int getIdHuesped() {
        return idHuesped;
    }

    public void setIdHuesped(int idHuesped) {
        this.idHuesped = idHuesped;
    }

    public int getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(int idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreHuesped() {
        return nombreHuesped;
    }

    public void setNombreHuesped(String nombreHuesped) {
        this.nombreHuesped = nombreHuesped;
    }

    public String getTipoHabitacion() {
        return tipoHabitacion;
    }

    public void setTipoHabitacion(String tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
    }
    
    // Sobrescribir toString para mostrar información en el combo
    @Override
    public String toString() {
        return "Reserva " + idReserva + " - " + nombreHuesped;
    }

    public void setPrecioHabitacion(BigDecimal precioHabitacion) {
    this.precioHabitacion = precioHabitacion;
}
    // Agregar este campo y métodos a Reserva.java
private BigDecimal precioHabitacion;

public BigDecimal getPrecioHabitacion() {
    return precioHabitacion;
}

}
