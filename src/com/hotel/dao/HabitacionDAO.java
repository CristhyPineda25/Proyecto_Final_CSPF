/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.dao;

/**
 *
 * @author pined
 */
import com.hotel.model.Habitacion;
import com.hotel.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HabitacionDAO {
    
    // Obtener todas las habitaciones
    public List<Habitacion> getAll() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Habitacion> habitaciones = new ArrayList<>();
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM habitaciones";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Habitacion habitacion = new Habitacion();
                habitacion.setIdHabitacion(rs.getInt("id_habitacion"));
                habitacion.setTipo(rs.getString("tipo"));
                habitacion.setPrecio(rs.getBigDecimal("precio"));
                habitacion.setCapacidad(rs.getInt("capacidad"));
                habitacion.setDisponible(rs.getBoolean("disponible"));
                habitaciones.add(habitacion);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las habitaciones: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
        
        return habitaciones;
    }
    
    // Obtener habitaciones disponibles
    public List<Habitacion> getAvailable() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Habitacion> habitaciones = new ArrayList<>();
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM habitaciones WHERE disponible = 1";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Habitacion habitacion = new Habitacion();
                habitacion.setIdHabitacion(rs.getInt("id_habitacion"));
                habitacion.setTipo(rs.getString("tipo"));
                habitacion.setPrecio(rs.getBigDecimal("precio"));
                habitacion.setCapacidad(rs.getInt("capacidad"));
                habitacion.setDisponible(rs.getBoolean("disponible"));
                habitaciones.add(habitacion);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener habitaciones disponibles: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
        
        return habitaciones;
    }
    
    // Obtener habitación por ID
    public Habitacion getById(int idHabitacion) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Habitacion habitacion = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM habitaciones WHERE id_habitacion = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idHabitacion);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                habitacion = new Habitacion();
                habitacion.setIdHabitacion(rs.getInt("id_habitacion"));
                habitacion.setTipo(rs.getString("tipo"));
                habitacion.setPrecio(rs.getBigDecimal("precio"));
                habitacion.setCapacidad(rs.getInt("capacidad"));
                habitacion.setDisponible(rs.getBoolean("disponible"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener habitación por ID: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
        
        return habitacion;
    }
    
    // Buscar habitaciones por tipo
    public List<Habitacion> searchByType(String searchText) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Habitacion> habitaciones = new ArrayList<>();
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM habitaciones WHERE tipo LIKE ?";
            stmt = conn.prepareStatement(sql);
            String pattern = "%" + searchText + "%";
            stmt.setString(1, pattern);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Habitacion habitacion = new Habitacion();
                habitacion.setIdHabitacion(rs.getInt("id_habitacion"));
                habitacion.setTipo(rs.getString("tipo"));
                habitacion.setPrecio(rs.getBigDecimal("precio"));
                habitacion.setCapacidad(rs.getInt("capacidad"));
                habitacion.setDisponible(rs.getBoolean("disponible"));
                habitaciones.add(habitacion);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar habitaciones por tipo: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
        
        return habitaciones;
    }
    
    // Insertar nueva habitación
    public boolean insert(Habitacion habitacion) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO habitaciones (tipo, precio, capacidad, disponible) " +
                         "VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, habitacion.getTipo());
            stmt.setBigDecimal(2, habitacion.getPrecio());
            stmt.setInt(3, habitacion.getCapacidad());
            stmt.setBoolean(4, habitacion.isDisponible());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    habitacion.setIdHabitacion(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error al insertar habitación: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
    }
    
    // Actualizar habitación
    public boolean update(Habitacion habitacion) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "UPDATE habitaciones SET tipo = ?, precio = ?, capacidad = ?, " +
                         "disponible = ? WHERE id_habitacion = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, habitacion.getTipo());
            stmt.setBigDecimal(2, habitacion.getPrecio());
            stmt.setInt(3, habitacion.getCapacidad());
            stmt.setBoolean(4, habitacion.isDisponible());
            stmt.setInt(5, habitacion.getIdHabitacion());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar habitación: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeResources(conn, stmt, null);
        }
    }
    
    // Eliminar habitación
    public boolean delete(int idHabitacion) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "DELETE FROM habitaciones WHERE id_habitacion = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idHabitacion);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar habitación: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeResources(conn, stmt, null);
        }
    }
}
 