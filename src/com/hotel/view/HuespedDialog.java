/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.view;

/**
 *
 * @author pined
 */
import com.hotel.dao.HuespedDAO;
import com.hotel.model.Huesped;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class HuespedDialog extends JDialog {
    
    private JPanel panelFormulario, panelBotones;
    private JTextField txtNombre, txtApellido, txtEmail, txtTelefono;
    private JTextArea txtDireccion;
    private JButton btnAceptar, btnCancelar;
    
    private HuespedDAO huespedDAO;
    private Huesped huesped;
    private boolean modoEdicion;
    private boolean aceptado;
    
    public HuespedDialog(JFrame parent, boolean modal) {
        super(parent, modal);
        huespedDAO = new HuespedDAO();
        huesped = new Huesped();
        modoEdicion = false;
        initComponents();
        configurarVentana();
        configurarEventos();
    }
    
    public HuespedDialog(JFrame parent, boolean modal, Huesped huesped) {
        super(parent, modal);
        huespedDAO = new HuespedDAO();
        this.huesped = huesped;
        modoEdicion = true;
        initComponents();
        configurarVentana();
        configurarEventos();
        cargarDatosHuesped();
    }
    
    private void initComponents() {
        panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lblNombre = new JLabel("Nombre:");
        JLabel lblApellido = new JLabel("Apellido:");
        JLabel lblEmail = new JLabel("Email:");
        JLabel lblTelefono = new JLabel("Teléfono:");
        JLabel lblDireccion = new JLabel("Dirección:");
        
        txtNombre = new JTextField(20);
        txtApellido = new JTextField(20);
        txtEmail = new JTextField(20);
        txtTelefono = new JTextField(20);
        txtDireccion = new JTextArea(3, 20);
        txtDireccion.setLineWrap(true);
        txtDireccion.setWrapStyleWord(true);
        JScrollPane scrollDireccion = new JScrollPane(txtDireccion);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panelFormulario.add(lblNombre, gbc);
        
        gbc.gridx = 1;
        panelFormulario.add(txtNombre, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelFormulario.add(lblApellido, gbc);
        
        gbc.gridx = 1;
        panelFormulario.add(txtApellido, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelFormulario.add(lblEmail, gbc);
        
        gbc.gridx = 1;
        panelFormulario.add(txtEmail, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelFormulario.add(lblTelefono, gbc);
        
        gbc.gridx = 1;
        panelFormulario.add(txtTelefono, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        panelFormulario.add(lblDireccion, gbc);
        
        gbc.gridx = 1;
        panelFormulario.add(scrollDireccion, gbc);
        
        panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        btnAceptar = new JButton("Aceptar");
        btnCancelar = new JButton("Cancelar");
        
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);
    }
    
    private void configurarVentana() {
        if (modoEdicion) {
            setTitle("Editar Huésped");
        } else {
            setTitle("Nuevo Huésped");
        }
        
        setSize(400, 300);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(getOwner());
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelFormulario, BorderLayout.CENTER);
        getContentPane().add(panelBotones, BorderLayout.SOUTH);
    }
    
    private void configurarEventos() {
        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarHuesped();
            }
        });
        
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aceptado = false;
                dispose();
            }
        });
    }
    
    private void cargarDatosHuesped() {
        txtNombre.setText(huesped.getNombre());
        txtApellido.setText(huesped.getApellido());
        txtEmail.setText(huesped.getEmail());
        txtTelefono.setText(huesped.getTelefono());
        txtDireccion.setText(huesped.getDireccion());
    }
    
    private void guardarHuesped() {
        // Validar campos
        if (txtNombre.getText().trim().isEmpty() ||  
            txtApellido.getText().trim().isEmpty() ||  
            txtEmail.getText().trim().isEmpty() ||  
            txtTelefono.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(
                    this, 
                    "Debe completar todos los campos obligatorios", 
                    "Validación", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        huesped.setNombre(txtNombre.getText().trim());
        huesped.setApellido(txtApellido.getText().trim());
        huesped.setEmail(txtEmail.getText().trim());
        huesped.setTelefono(txtTelefono.getText().trim());
        huesped.setDireccion(txtDireccion.getText().trim());
        
        boolean resultado;
        
        if (modoEdicion) {
            resultado = huespedDAO.update(huesped);
        } else {
            resultado = huespedDAO.insert(huesped);
        }
        
        if (resultado) {
            JOptionPane.showMessageDialog(
                    this, 
                    "Huésped guardado correctamente", 
                    "Guardar huésped", 
                    JOptionPane.INFORMATION_MESSAGE);
            
            aceptado = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                    this, 
                    "Error al guardar el huésped", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isAceptado() {
        return aceptado;
    }
}