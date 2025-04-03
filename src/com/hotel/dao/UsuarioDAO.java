/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.dao;

/**
 *
 * @author pined
 */
import com.hotel.model.Usuario;
import com.hotel.util.DatabaseConnection;
import com.hotel.util.PasswordUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    
    // Obtener usuario por nombre de usuario
    public Usuario getByUsername(String username) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT u.*, r.nombre_rol FROM usuarios u " +
                         "JOIN roles r ON u.id_rol = r.id_rol " +
                         "WHERE u.usuario = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setIdEmpleado(rs.getInt("id_empleado"));
                usuario.setUsuario(rs.getString("usuario"));
                usuario.setContrasena(rs.getString("contrasena"));
                usuario.setSalt(rs.getString("salt"));
                usuario.setIdRol(rs.getInt("id_rol"));
                usuario.setNombreRol(rs.getString("nombre_rol"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por nombre: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
        
        return usuario;
    }
    
    // Verificar credenciales de usuario
    public boolean verifyCredentials(String username, String password) {
        Usuario usuario = getByUsername(username);
        
        if (usuario == null) {
            return false;
        }
        
        // Si el usuario no tiene salt (migración de contraseñas antiguas)
        if (usuario.getSalt() == null || usuario.getSalt().isEmpty()) {
            // Actualizar a nuevo formato con salt
            String salt = PasswordUtil.generateSalt();
            String hashedPassword = PasswordUtil.hashPassword(password, salt);
            
            // Si la contraseña coincide con la almacenada (sin salt)
            if (password.equals(usuario.getContrasena())) {
                // Actualizar la contraseña con salt
                updatePassword(usuario.getIdUsuario(), hashedPassword, salt);
                return true;
            }
            return false;
        }
        
        // Verificar contraseña con salt
        return PasswordUtil.verifyPassword(password, usuario.getSalt(), usuario.getContrasena());
    }
    
    // Actualizar contraseña de usuario
    private boolean updatePassword(int idUsuario, String hashedPassword, String salt) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "UPDATE usuarios SET contrasena = ?, salt = ? WHERE id_usuario = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, hashedPassword);
            stmt.setString(2, salt);
            stmt.setInt(3, idUsuario);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar contraseña: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeResources(conn, stmt, null);
        }
    }
    
    // Obtener todos los usuarios
    public List<Usuario> getAll() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Usuario> usuarios = new ArrayList<>();
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT u.*, r.nombre_rol FROM usuarios u " +
                         "JOIN roles r ON u.id_rol = r.id_rol";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setIdEmpleado(rs.getInt("id_empleado"));
                usuario.setUsuario(rs.getString("usuario"));
                usuario.setContrasena(rs.getString("contrasena"));
                usuario.setSalt(rs.getString("salt"));
                usuario.setIdRol(rs.getInt("id_rol"));
                usuario.setNombreRol(rs.getString("nombre_rol"));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los usuarios: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
        
        return usuarios;
    }
    
    // Insertar nuevo usuario
    public boolean insert(Usuario usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            // Generar salt y hash de contraseña
            String salt = PasswordUtil.generateSalt();
            String hashedPassword = PasswordUtil.hashPassword(usuario.getContrasena(), salt);
            
            conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO usuarios (id_empleado, usuario, contrasena, salt, id_rol) " +
                         "VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, usuario.getIdEmpleado());
            stmt.setString(2, usuario.getUsuario());
            stmt.setString(3, hashedPassword);
            stmt.setString(4, salt);
            stmt.setInt(5, usuario.getIdRol());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    usuario.setIdUsuario(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
    }
    
    // Actualizar usuario
    public boolean update(Usuario usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "UPDATE usuarios SET id_empleado = ?, usuario = ?, id_rol = ? " +
                         "WHERE id_usuario = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, usuario.getIdEmpleado());
            stmt.setString(2, usuario.getUsuario());
            stmt.setInt(3, usuario.getIdRol());
            stmt.setInt(4, usuario.getIdUsuario());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeResources(conn, stmt, null);
        }
    }
    
    // Eliminar usuario
    public boolean delete(int idUsuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeResources(conn, stmt, null);
        }
    }
}