/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.dao;

/**
 *
 * @author pined
 */
import com.hotel.model.Reserva;
import com.hotel.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservaDAO {
    
    // Obtener todas las reservas (método interno)
    public List<Reserva> getAll() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Reserva> reservas = new ArrayList<>();
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT r.*, h.nombre, h.apellido, hab.tipo " +
                         "FROM reservas r " +
                         "JOIN huespedes h ON r.id_huesped = h.id_huesped " +
                         "JOIN habitaciones hab ON r.id_habitacion = hab.id_habitacion";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Reserva reserva = new Reserva();
                reserva.setIdReserva(rs.getInt("id_reserva"));
                reserva.setIdHuesped(rs.getInt("id_huesped"));
                reserva.setIdHabitacion(rs.getInt("id_habitacion"));
                reserva.setFechaInicio(rs.getDate("fecha_inicio"));
                reserva.setFechaFin(rs.getDate("fecha_fin"));
                reserva.setEstado(rs.getString("estado"));
                reserva.setNombreHuesped(rs.getString("nombre") + " " + rs.getString("apellido"));
                reserva.setTipoHabitacion(rs.getString("tipo"));
                reservas.add(reserva);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las reservas: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
        
        return reservas;
    }
    
    // Método corregido: Obtener todas las reservas (delegando a getAll)
    public List<Reserva> obtenerTodas() {
        return getAll();
    }
    
    // Obtener reserva por ID
    public Reserva getById(int idReserva) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Reserva reserva = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT r.*, h.nombre, h.apellido, hab.tipo " +
                         "FROM reservas r " +
                         "JOIN huespedes h ON r.id_huesped = h.id_huesped " +
                         "JOIN habitaciones hab ON r.id_habitacion = hab.id_habitacion " +
                         "WHERE r.id_reserva = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idReserva);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                reserva = new Reserva();
                reserva.setIdReserva(rs.getInt("id_reserva"));
                reserva.setIdHuesped(rs.getInt("id_huesped"));
                reserva.setIdHabitacion(rs.getInt("id_habitacion"));
                reserva.setFechaInicio(rs.getDate("fecha_inicio"));
                reserva.setFechaFin(rs.getDate("fecha_fin"));
                reserva.setEstado(rs.getString("estado"));
                reserva.setNombreHuesped(rs.getString("nombre") + " " + rs.getString("apellido"));
                reserva.setTipoHabitacion(rs.getString("tipo"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener reserva por ID: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
        
        return reserva;
    }
    
    // Buscar reservas por estado
    public List<Reserva> searchByStatus(String status) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Reserva> reservas = new ArrayList<>();
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT r.*, h.nombre, h.apellido, hab.tipo " +
                         "FROM reservas r " +
                         "JOIN huespedes h ON r.id_huesped = h.id_huesped " +
                         "JOIN habitaciones hab ON r.id_habitacion = hab.id_habitacion " +
                         "WHERE r.estado LIKE ?";
            stmt = conn.prepareStatement(sql);
            String pattern = "%" + status + "%";
            stmt.setString(1, pattern);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Reserva reserva = new Reserva();
                reserva.setIdReserva(rs.getInt("id_reserva"));
                reserva.setIdHuesped(rs.getInt("id_huesped"));
                reserva.setIdHabitacion(rs.getInt("id_habitacion"));
                reserva.setFechaInicio(rs.getDate("fecha_inicio"));
                reserva.setFechaFin(rs.getDate("fecha_fin"));
                reserva.setEstado(rs.getString("estado"));
                reserva.setNombreHuesped(rs.getString("nombre") + " " + rs.getString("apellido"));
                reserva.setTipoHabitacion(rs.getString("tipo"));
                reservas.add(reserva);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar reservas por estado: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
        
        return reservas;
    }
    
    // Verificar disponibilidad de habitación
    public boolean checkAvailability(int idHabitacion, Date fechaInicio, Date fechaFin) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM reservas " +
                         "WHERE id_habitacion = ? AND " +
                         "((fecha_inicio BETWEEN ? AND ?) OR " +
                         "(fecha_fin BETWEEN ? AND ?) OR " +
                         "(fecha_inicio <= ? AND fecha_fin >= ?))";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idHabitacion);
            stmt.setDate(2, new java.sql.Date(fechaInicio.getTime()));
            stmt.setDate(3, new java.sql.Date(fechaFin.getTime()));
            stmt.setDate(4, new java.sql.Date(fechaInicio.getTime()));
            stmt.setDate(5, new java.sql.Date(fechaFin.getTime()));
            stmt.setDate(6, new java.sql.Date(fechaInicio.getTime()));
            stmt.setDate(7, new java.sql.Date(fechaFin.getTime()));
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) == 0; // Si no hay reservas que se solapen, está disponible
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar disponibilidad: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
        
        return false; // Por defecto, asumimos que no está disponible
    }
    
    // Insertar nueva reserva
    public boolean insert(Reserva reserva) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO reservas (id_huesped, id_habitacion, fecha_inicio, fecha_fin, estado) " +
                         "VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, reserva.getIdHuesped());
            stmt.setInt(2, reserva.getIdHabitacion());
            stmt.setDate(3, new java.sql.Date(reserva.getFechaInicio().getTime()));
            stmt.setDate(4, new java.sql.Date(reserva.getFechaFin().getTime()));
            stmt.setString(5, reserva.getEstado());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    reserva.setIdReserva(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error al insertar reserva: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
    }
    
    // Actualizar reserva
    public boolean update(Reserva reserva) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "UPDATE reservas SET id_huesped = ?, id_habitacion = ?, " +
                         "fecha_inicio = ?, fecha_fin = ?, estado = ? " +
                         "WHERE id_reserva = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, reserva.getIdHuesped());
            stmt.setInt(2, reserva.getIdHabitacion());
            stmt.setDate(3, new java.sql.Date(reserva.getFechaInicio().getTime()));
            stmt.setDate(4, new java.sql.Date(reserva.getFechaFin().getTime()));
            stmt.setString(5, reserva.getEstado());
            stmt.setInt(6, reserva.getIdReserva());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar reserva: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeResources(conn, stmt, null);
        }
    }
    
    // Eliminar reserva
    public boolean delete(int idReserva) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "DELETE FROM reservas WHERE id_reserva = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idReserva);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar reserva: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeResources(conn, stmt, null);
        }
    }
    // Agregar este método a ReservaDAO.java
public List<Reserva> getReservasConfirmadas() {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    List<Reserva> reservas = new ArrayList<>();
    
    try {
        conn = DatabaseConnection.getConnection();
        stmt = conn.createStatement();
        String sql = "SELECT r.*, h.nombre, h.apellido, hab.tipo, hab.precio " +
                     "FROM reservas r " +
                     "JOIN huespedes h ON r.id_huesped = h.id_huesped " +
                     "JOIN habitaciones hab ON r.id_habitacion = hab.id_habitacion " +
                     "WHERE r.estado = 'Confirmada'";
        rs = stmt.executeQuery(sql);
        
        while (rs.next()) {
            Reserva reserva = new Reserva();
            reserva.setIdReserva(rs.getInt("id_reserva"));
            reserva.setIdHuesped(rs.getInt("id_huesped"));
            reserva.setIdHabitacion(rs.getInt("id_habitacion"));
            reserva.setFechaInicio(rs.getDate("fecha_inicio"));
            reserva.setFechaFin(rs.getDate("fecha_fin"));
            reserva.setEstado(rs.getString("estado"));
            reserva.setNombreHuesped(rs.getString("nombre") + " " + rs.getString("apellido"));
            reserva.setTipoHabitacion(rs.getString("tipo"));
            // Agregar precio como campo adicional
            reserva.setPrecioHabitacion(rs.getBigDecimal("precio"));
            reservas.add(reserva);
        }
    } catch (SQLException e) {
        System.err.println("Error al obtener reservas confirmadas: " + e.getMessage());
    } finally {
        DatabaseConnection.closeResources(conn, stmt, rs);
    }
    
    return reservas;
}
}
