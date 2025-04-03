/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.dao;

/**
 *
 * @author pined
 */
import com.hotel.model.Huesped;
import com.hotel.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HuespedDAO {
    
    // Obtener todos los huéspedes
    public List<Huesped> getAll() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Huesped> huespedes = new ArrayList<>();
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM huespedes";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Huesped huesped = new Huesped();
                huesped.setIdHuesped(rs.getInt("id_huesped"));
                huesped.setNombre(rs.getString("nombre"));
                huesped.setApellido(rs.getString("apellido"));
                huesped.setEmail(rs.getString("email"));
                huesped.setTelefono(rs.getString("telefono"));
                huesped.setDireccion(rs.getString("direccion"));
                huespedes.add(huesped);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los huéspedes: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
        
        return huespedes;
    }
    
    // Obtener huésped por ID
    public Huesped getById(int idHuesped) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Huesped huesped = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM huespedes WHERE id_huesped = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idHuesped);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                huesped = new Huesped();
                huesped.setIdHuesped(rs.getInt("id_huesped"));
                huesped.setNombre(rs.getString("nombre"));
                huesped.setApellido(rs.getString("apellido"));
                huesped.setEmail(rs.getString("email"));
                huesped.setTelefono(rs.getString("telefono"));
                huesped.setDireccion(rs.getString("direccion"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener huésped por ID: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
        
        return huesped;
    }
    
    // Buscar huéspedes por nombre o apellido
    public List<Huesped> searchByName(String searchText) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Huesped> huespedes = new ArrayList<>();
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM huespedes WHERE nombre LIKE ? OR apellido LIKE ?";
            stmt = conn.prepareStatement(sql);
            String pattern = "%" + searchText + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Huesped huesped = new Huesped();
                huesped.setIdHuesped(rs.getInt("id_huesped"));
                huesped.setNombre(rs.getString("nombre"));
                huesped.setApellido(rs.getString("apellido"));
                huesped.setEmail(rs.getString("email"));
                huesped.setTelefono(rs.getString("telefono"));
                huesped.setDireccion(rs.getString("direccion"));
                huespedes.add(huesped);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar huéspedes por nombre: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
        
        return huespedes;
    }
    
    // Insertar nuevo huésped
    public boolean insert(Huesped huesped) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO huespedes (nombre, apellido, email, telefono, direccion) " +
                         "VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, huesped.getNombre());
            stmt.setString(2, huesped.getApellido());
            stmt.setString(3, huesped.getEmail());
            stmt.setString(4, huesped.getTelefono());
            stmt.setString(5, huesped.getDireccion());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    huesped.setIdHuesped(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error al insertar huésped: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
    }
    
    // Actualizar huésped
    public boolean update(Huesped huesped) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "UPDATE huespedes SET nombre = ?, apellido = ?, email = ?, " +
                         "telefono = ?, direccion = ? WHERE id_huesped = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, huesped.getNombre());
            stmt.setString(2, huesped.getApellido());
            stmt.setString(3, huesped.getEmail());
            stmt.setString(4, huesped.getTelefono());
            stmt.setString(5, huesped.getDireccion());
            stmt.setInt(6, huesped.getIdHuesped());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar huésped: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeResources(conn, stmt, null);
        }
    }
    
    // Eliminar huésped
    public boolean delete(int idHuesped) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "DELETE FROM huespedes WHERE id_huesped = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idHuesped);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar huésped: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeResources(conn, stmt, null);
        }
    }
}