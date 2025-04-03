/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.hotel.view;

import com.hotel.dao.PagoDAO;
import com.hotel.dao.ReservaDAO;
import com.hotel.model.Pago;
import com.hotel.model.Reserva;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.swing.*;

public class PagoDialog extends JDialog {
    
    private JPanel panelFormulario, panelBotones, panelInfoReserva;
    private JComboBox<Reserva> cboReserva;
    private JTextField txtMonto;
    private JDateChooser fechaPago;
    private JComboBox<String> cboMetodoPago;
    private JButton btnAceptar, btnCancelar, btnCalcular;
    private JLabel lblDias, lblPrecioNoche, lblTotal;
    
    private PagoDAO pagoDAO;
    private ReservaDAO reservaDAO;
    private Pago pago;
    private boolean modoEdicion;
    private boolean aceptado;
    
    public PagoDialog(JFrame parent, boolean modal) {
        super(parent, modal);
        pagoDAO = new PagoDAO();
        reservaDAO = new ReservaDAO();
        pago = new Pago();
        modoEdicion = false;
        initComponents();
        configurarVentana();
        configurarEventos();
        cargarCombos();
    }
    
    public PagoDialog(JFrame parent, boolean modal, Pago pago) {
        super(parent, modal);
        pagoDAO = new PagoDAO();
        reservaDAO = new ReservaDAO();
        this.pago = pago;
        modoEdicion = true;
        initComponents();
        configurarVentana();
        configurarEventos();
        cargarCombos();
        cargarDatosPago();
    }
    
    private void initComponents() {
        panelFormulario = new JPanel(new GridBagLayout());
        panelInfoReserva = new JPanel(new GridLayout(3, 2, 5, 5));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lblReserva = new JLabel("Reserva:");
        JLabel lblMonto = new JLabel("Monto:");
        JLabel lblFechaPago = new JLabel("Fecha Pago:");
        JLabel lblMetodoPago = new JLabel("Método Pago:");
        
        cboReserva = new JComboBox<>();
        txtMonto = new JTextField(20);
        txtMonto.setEditable(false);
        fechaPago = new JDateChooser();
        fechaPago.setDate(new Date());
        
        cboMetodoPago = new JComboBox<>(new String[]{"Efectivo", "Tarjeta", "Transferencia"});
        btnCalcular = new JButton("Calcular");
        
        // Panel de información de reserva
        panelInfoReserva.add(new JLabel("Días de estadía:"));
        lblDias = new JLabel("0");
        panelInfoReserva.add(lblDias);
        
        panelInfoReserva.add(new JLabel("Precio por noche:"));
        lblPrecioNoche = new JLabel("$0.00");
        panelInfoReserva.add(lblPrecioNoche);
        
        panelInfoReserva.add(new JLabel("Total a pagar:"));
        lblTotal = new JLabel("$0.00");
        panelInfoReserva.add(lblTotal);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelFormulario.add(lblReserva, gbc);
        gbc.gridx = 1;
        panelFormulario.add(cboReserva, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelFormulario.add(lblMonto, gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtMonto, gbc);
        gbc.gridx = 2;
        panelFormulario.add(btnCalcular, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelFormulario.add(lblFechaPago, gbc);
        gbc.gridx = 1;
        panelFormulario.add(fechaPago, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelFormulario.add(lblMetodoPago, gbc);
        gbc.gridx = 1;
        panelFormulario.add(cboMetodoPago, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        panelFormulario.add(panelInfoReserva, gbc);
        gbc.gridwidth = 1;
        
        panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAceptar = new JButton("Aceptar");
        btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);
        
        setLayout(new BorderLayout());
        add(panelFormulario, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private void configurarVentana() {
        setTitle(modoEdicion ? "Editar Pago" : "Registrar Pago");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void configurarEventos() {
        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarPago();
            }
        });
        
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aceptado = false;
                dispose();
            }
        });
        
        btnCalcular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularMonto();
            }
        });
        
        cboReserva.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarInfoReserva();
            }
        });
    }
    
    private void cargarCombos() {
        List<Reserva> reservas = reservaDAO.getReservasConfirmadas();
        DefaultComboBoxModel<Reserva> modelo = new DefaultComboBoxModel<>();
        
        for (Reserva r : reservas) {
            // Solo mostrar reservas que no tienen pagos asociados
            if (!pagoDAO.tienePagos(r.getIdReserva())) {
                modelo.addElement(r);
            }
        }
        
        cboReserva.setModel(modelo);
        
        if (modoEdicion && pago.getIdReserva() > 0) {
            // En modo edición, mostrar la reserva del pago aunque ya tenga pagos
            for (int i = 0; i < modelo.getSize(); i++) {
                if (modelo.getElementAt(i).getIdReserva() == pago.getIdReserva()) {
                    cboReserva.setSelectedIndex(i);
                    break;
                }
            }
        }
    }
    
    private void cargarDatosPago() {
        if (pago.getIdReserva() > 0) {
            for (int i = 0; i < cboReserva.getItemCount(); i++) {
                if (cboReserva.getItemAt(i).getIdReserva() == pago.getIdReserva()) {
                    cboReserva.setSelectedIndex(i);
                    break;
                }
            }
        }
        txtMonto.setText(pago.getMonto() != null ? pago.getMonto().toString() : "");
        fechaPago.setDate(pago.getFechaPago());
        cboMetodoPago.setSelectedItem(pago.getMetodoPago());
    }
    
    private void mostrarInfoReserva() {
        Reserva reservaSeleccionada = (Reserva) cboReserva.getSelectedItem();
        if (reservaSeleccionada != null) {
            long diff = reservaSeleccionada.getFechaFin().getTime() - 
                       reservaSeleccionada.getFechaInicio().getTime();
            long dias = diff / (1000 * 60 * 60 * 24);
            
            lblDias.setText(String.valueOf(dias));
            lblPrecioNoche.setText("$" + reservaSeleccionada.getPrecioHabitacion().toString());
            
            BigDecimal total = reservaSeleccionada.getPrecioHabitacion().multiply(new BigDecimal(dias));
            lblTotal.setText("$" + total.toString());
        }
    }
    
    private void calcularMonto() {
        Reserva reservaSeleccionada = (Reserva) cboReserva.getSelectedItem();
        if (reservaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, 
                    "Seleccione una reserva primero", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        long diff = reservaSeleccionada.getFechaFin().getTime() - 
                   reservaSeleccionada.getFechaInicio().getTime();
        long dias = diff / (1000 * 60 * 60 * 24);
        
        BigDecimal monto = reservaSeleccionada.getPrecioHabitacion().multiply(new BigDecimal(dias));
        txtMonto.setText(monto.toString());
    }
    
    private void guardarPago() {
        try {
            Reserva reservaSeleccionada = (Reserva) cboReserva.getSelectedItem();
            if (reservaSeleccionada == null) {
                JOptionPane.showMessageDialog(this, 
                        "Debe seleccionar una reserva", 
                        "Validación", 
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (txtMonto.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                        "Debe calcular el monto primero", 
                        "Validación", 
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            pago.setIdReserva(reservaSeleccionada.getIdReserva());
            pago.setMonto(new BigDecimal(txtMonto.getText()));
            pago.setFechaPago(fechaPago.getDate());
            pago.setMetodoPago(cboMetodoPago.getSelectedItem().toString());
            
            if (modoEdicion) {
                pagoDAO.actualizar(pago);
                JOptionPane.showMessageDialog(this, "Pago actualizado correctamente.");
            } else {
                pagoDAO.insertar(pago);
                JOptionPane.showMessageDialog(this, "Pago registrado correctamente.");
            }
            aceptado = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                    "Error al guardar el pago: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isAceptado() {
        return aceptado;
    }
}