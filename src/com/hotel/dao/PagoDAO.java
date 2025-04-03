/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.dao;

/**
 *
 * @author pined
 */
import com.hotel.model.Pago;
import com.hotel.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO {
    
    // Obtener todos los pagos
    public List<Pago> getAll() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Pago> pagos = new ArrayList<>();
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT p.*, r.id_huesped, h.nombre, h.apellido " +
                         "FROM pagos p " +
                         "JOIN reservas r ON p.id_reserva = r.id_reserva " +
                         "JOIN huespedes h ON r.id_huesped = h.id_huesped";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Pago pago = new Pago();
                pago.setIdPago(rs.getInt("id_pago"));
                pago.setIdReserva(rs.getInt("id_reserva"));
                pago.setMonto(rs.getBigDecimal("monto"));
                pago.setFechaPago(rs.getDate("fecha_pago"));
                pago.setMetodoPago(rs.getString("metodo_pago"));
                pago.setNombreHuesped(rs.getString("nombre") + " " + rs.getString("apellido"));
                pagos.add(pago);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los pagos: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
        
        return pagos;
    }
    
    // Obtener pago por ID
    public Pago getById(int idPago) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Pago pago = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT p.*, r.id_huesped, h.nombre, h.apellido " +
                         "FROM pagos p " +
                         "JOIN reservas r ON p.id_reserva = r.id_reserva " +
                         "JOIN huespedes h ON r.id_huesped = h.id_huesped " +
                         "WHERE p.id_pago = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idPago);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                pago = new Pago();
                pago.setIdPago(rs.getInt("id_pago"));
                pago.setIdReserva(rs.getInt("id_reserva"));
                pago.setMonto(rs.getBigDecimal("monto"));
                pago.setFechaPago(rs.getDate("fecha_pago"));
                pago.setMetodoPago(rs.getString("metodo_pago"));
                pago.setNombreHuesped(rs.getString("nombre") + " " + rs.getString("apellido"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener pago por ID: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
        
        return pago;
    }
    
    // Obtener pagos por reserva
    public List<Pago> getByReserva(int idReserva) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Pago> pagos = new ArrayList<>();
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT p.*, r.id_huesped, h.nombre, h.apellido " +
                         "FROM pagos p " +
                         "JOIN reservas r ON p.id_reserva = r.id_reserva " +
                         "JOIN huespedes h ON r.id_huesped = h.id_huesped " +
                         "WHERE p.id_reserva = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idReserva);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Pago pago = new Pago();
                pago.setIdPago(rs.getInt("id_pago"));
                pago.setIdReserva(rs.getInt("id_reserva"));
                pago.setMonto(rs.getBigDecimal("monto"));
                pago.setFechaPago(rs.getDate("fecha_pago"));
                pago.setMetodoPago(rs.getString("metodo_pago"));
                pago.setNombreHuesped(rs.getString("nombre") + " " + rs.getString("apellido"));
                pagos.add(pago);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener pagos por reserva: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
        
        return pagos;
    }
    
    // Insertar nuevo pago (retorna boolean)
    public boolean insert(Pago pago) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO pagos (id_reserva, monto, fecha_pago, metodo_pago) " +
                         "VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, pago.getIdReserva());
            stmt.setBigDecimal(2, pago.getMonto());
            stmt.setDate(3, new java.sql.Date(pago.getFechaPago().getTime()));
            stmt.setString(4, pago.getMetodoPago());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    pago.setIdPago(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error al insertar pago: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
    }
    
    // Actualizar pago (retorna boolean)
    public boolean update(Pago pago) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "UPDATE pagos SET id_reserva = ?, monto = ?, fecha_pago = ?, " +
                         "metodo_pago = ? WHERE id_pago = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, pago.getIdReserva());
            stmt.setBigDecimal(2, pago.getMonto());
            stmt.setDate(3, new java.sql.Date(pago.getFechaPago().getTime()));
            stmt.setString(4, pago.getMetodoPago());
            stmt.setInt(5, pago.getIdPago());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar pago: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeResources(conn, stmt, null);
        }
    }
    
    // Eliminar pago
    public boolean delete(int idPago) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "DELETE FROM pagos WHERE id_pago = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idPago);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar pago: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeResources(conn, stmt, null);
        }
    }
    
    // Versión void que lanza excepción si falla, delegando en el método booleano insert
    public void insertar(Pago pago) {
        boolean exito = insert(pago);
        if (!exito) {
            throw new RuntimeException("Error al insertar el pago.");
        }
    }
    
    // Versión void que lanza excepción si falla, delegando en el método booleano update
    public void actualizar(Pago pago) {
        boolean exito = update(pago);
        if (!exito) {
            throw new RuntimeException("Error al actualizar el pago.");
        }
    }
    // Agregar este método a PagoDAO.java
public boolean tienePagos(int idReserva) {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    
    try {
        conn = DatabaseConnection.getConnection();
        String sql = "SELECT COUNT(*) FROM pagos WHERE id_reserva = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idReserva);
        rs = stmt.executeQuery();
        
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        System.err.println("Error al verificar pagos de reserva: " + e.getMessage());
    } finally {
        DatabaseConnection.closeResources(conn, stmt, rs);
    }
    
    return false;
}
}

