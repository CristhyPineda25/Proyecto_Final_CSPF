/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.view;

/**
 *
 * @author pined
 */
import com.hotel.dao.HabitacionDAO;
import com.hotel.model.Habitacion;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class HabitacionDialog extends JDialog {
    
    private JPanel panelFormulario, panelBotones;
    private JTextField txtTipo, txtPrecio, txtCapacidad;
    private JComboBox<String> cboEstado;
    private JButton btnAceptar, btnCancelar;
    
    private HabitacionDAO habitacionDAO;
    private Habitacion habitacion;
    private boolean modoEdicion;
    private boolean aceptado;
    
    public HabitacionDialog(JFrame parent, boolean modal) {
        super(parent, modal);
        habitacionDAO = new HabitacionDAO();
        habitacion = new Habitacion();
        modoEdicion = false;
        initComponents();
        configurarVentana();
        configurarEventos();
    }
    
    public HabitacionDialog(JFrame parent, boolean modal, Habitacion habitacion) {
        super(parent, modal);
        habitacionDAO = new HabitacionDAO();
        this.habitacion = habitacion;
        modoEdicion = true;
        initComponents();
        configurarVentana();
        configurarEventos();
        cargarDatosHabitacion();
    }
    
    private void initComponents() {
        panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lblTipo = new JLabel("Tipo:");
        JLabel lblPrecio = new JLabel("Precio:");
        JLabel lblCapacidad = new JLabel("Capacidad:");
        JLabel lblEstado = new JLabel("Estado:");
        
        txtTipo = new JTextField(20);
        txtPrecio = new JTextField(20);
        txtCapacidad = new JTextField(20);
        
        cboEstado = new JComboBox<>();
        cboEstado.addItem("Disponible");
        cboEstado.addItem("No Disponible");
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panelFormulario.add(lblTipo, gbc);
        
        gbc.gridx = 1;
        panelFormulario.add(txtTipo, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelFormulario.add(lblPrecio, gbc);
        
        gbc.gridx = 1;
        panelFormulario.add(txtPrecio, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelFormulario.add(lblCapacidad, gbc);
        
        gbc.gridx = 1;
        panelFormulario.add(txtCapacidad, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelFormulario.add(lblEstado, gbc);
        
        gbc.gridx = 1;
        panelFormulario.add(cboEstado, gbc);
        
        panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        btnAceptar = new JButton("Aceptar");
        btnCancelar = new JButton("Cancelar");
        
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);
    }
    
    private void configurarVentana() {
        if (modoEdicion) {
            setTitle("Editar Habitación");
        } else {
            setTitle("Nueva Habitación");
        }
        
        setSize(400, 250);
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
                guardarHabitacion();
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
    
    private void cargarDatosHabitacion() {
        txtTipo.setText(habitacion.getTipo());
        txtPrecio.setText(habitacion.getPrecio().toString());
        txtCapacidad.setText(String.valueOf(habitacion.getCapacidad()));
        cboEstado.setSelectedItem(habitacion.getEstado());
    }
    
    private void guardarHabitacion() {
        // Validar campos
        if (txtTipo.getText().trim().isEmpty() ||  
            txtPrecio.getText().trim().isEmpty() ||  
            txtCapacidad.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(
                    this, 
                    "Debe completar todos los campos obligatorios", 
                    "Validación", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            BigDecimal precio = new BigDecimal(txtPrecio.getText().trim());
            int capacidad = Integer.parseInt(txtCapacidad.getText().trim());
            
            if (precio.compareTo(BigDecimal.ZERO) <= 0 || capacidad <= 0) {
                JOptionPane.showMessageDialog(
                        this, 
                        "Los valores numéricos deben ser mayores que cero", 
                        "Validación", 
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            habitacion.setTipo(txtTipo.getText().trim());
            habitacion.setPrecio(precio);
            habitacion.setCapacidad(capacidad);
            habitacion.setEstado(cboEstado.getSelectedItem().toString());
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this, 
                    "Los campos Precio y Capacidad deben ser numéricos", 
                    "Validación", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        boolean resultado;
        
        if (modoEdicion) {
            resultado = habitacionDAO.update(habitacion);
        } else {
            resultado = habitacionDAO.insert(habitacion);
        }
        
        if (resultado) {
            JOptionPane.showMessageDialog(
                    this, 
                    "Habitación guardada correctamente", 
                    "Guardar habitación", 
                    JOptionPane.INFORMATION_MESSAGE);
            
            aceptado = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                    this, 
                    "Error al guardar la habitación", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isAceptado() {
        return aceptado;
    }
}