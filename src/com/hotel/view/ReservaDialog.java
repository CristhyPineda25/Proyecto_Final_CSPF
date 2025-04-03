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
import com.hotel.dao.HuespedDAO;
import com.hotel.dao.ReservaDAO;
import com.hotel.model.Habitacion;
import com.hotel.model.Huesped;
import com.hotel.model.Reserva;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ReservaDialog extends JDialog {
    
    private JPanel panelFormulario, panelBotones;
    private JComboBox<Huesped> cboHuesped;
    private JComboBox<Habitacion> cboHabitacion;
    private JDateChooser fechaInicio, fechaFin;
    private JComboBox<String> cboEstado;
    private JButton btnAceptar, btnCancelar;
    
    private ReservaDAO reservaDAO;
    private HuespedDAO huespedDAO;
    private HabitacionDAO habitacionDAO;
    private Reserva reserva;
    private boolean modoEdicion;
    private boolean aceptado;
    
    public ReservaDialog(JFrame parent, boolean modal) {
        super(parent, modal);
        reservaDAO = new ReservaDAO();
        huespedDAO = new HuespedDAO();
        habitacionDAO = new HabitacionDAO();
        reserva = new Reserva();
        modoEdicion = false;
        initComponents();
        configurarVentana();
        configurarEventos();
        cargarCombos();
    }
    
    public ReservaDialog(JFrame parent, boolean modal, Reserva reserva) {
        super(parent, modal);
        reservaDAO = new ReservaDAO();
        huespedDAO = new HuespedDAO();
        habitacionDAO = new HabitacionDAO();
        this.reserva = reserva;
        modoEdicion = true;
        initComponents();
        configurarVentana();
        configurarEventos();
        cargarCombos();
        cargarDatosReserva();
    }
    
    private void initComponents() {
        panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lblHuesped = new JLabel("Huésped:");
        JLabel lblHabitacion = new JLabel("Habitación:");
        JLabel lblFechaInicio = new JLabel("Fecha Inicio:");
        JLabel lblFechaFin = new JLabel("Fecha Fin:");
        JLabel lblEstado = new JLabel("Estado:");
        
        cboHuesped = new JComboBox<>();
        cboHabitacion = new JComboBox<>();
        fechaInicio = new JDateChooser();
        fechaFin = new JDateChooser();
        
        cboEstado = new JComboBox<>();
        cboEstado.addItem("Confirmada");
        cboEstado.addItem("Pendiente");
        cboEstado.addItem("Cancelada");
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panelFormulario.add(lblHuesped, gbc);
        
        gbc.gridx = 1;
        panelFormulario.add(cboHuesped, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelFormulario.add(lblHabitacion, gbc);
        
        gbc.gridx = 1;
        panelFormulario.add(cboHabitacion, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelFormulario.add(lblFechaInicio, gbc);
        
        gbc.gridx = 1;
        panelFormulario.add(fechaInicio, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelFormulario.add(lblFechaFin, gbc);
        
        gbc.gridx = 1;
        panelFormulario.add(fechaFin, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
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
            setTitle("Editar Reserva");
        } else {
            setTitle("Nueva Reserva");
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
                guardarReserva();
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
    
    private void cargarCombos() {
        // Cargar huéspedes
        List<Huesped> huespedes = huespedDAO.getAll();
        DefaultComboBoxModel<Huesped> modeloHuespedes = new DefaultComboBoxModel<>();
        
        for (Huesped huesped : huespedes) {
            modeloHuespedes.addElement(huesped);
        }
        
        cboHuesped.setModel(modeloHuespedes);
        
        // Cargar habitaciones
        List<Habitacion> habitaciones = habitacionDAO.getAll();
        DefaultComboBoxModel<Habitacion> modeloHabitaciones = new DefaultComboBoxModel<>();
        
        for (Habitacion habitacion : habitaciones) {
            modeloHabitaciones.addElement(habitacion);
        }
        
        cboHabitacion.setModel(modeloHabitaciones);
    }
    
    private void cargarDatosReserva() {
        // Seleccionar huésped
        for (int i = 0; i < cboHuesped.getItemCount(); i++) {
            Huesped huesped = cboHuesped.getItemAt(i);
            if (huesped.getIdHuesped() == reserva.getIdHuesped()) {
                cboHuesped.setSelectedIndex(i);
                break;
            }
        }
        
        // Seleccionar habitación
        for (int i = 0; i < cboHabitacion.getItemCount(); i++) {
            Habitacion habitacion = cboHabitacion.getItemAt(i);
            if (habitacion.getIdHabitacion() == reserva.getIdHabitacion()) {
                cboHabitacion.setSelectedIndex(i);
                break;
            }
        }
        
        // Establecer fechas
        fechaInicio.setDate(reserva.getFechaInicio());
        fechaFin.setDate(reserva.getFechaFin());
        
        // Establecer estado
        cboEstado.setSelectedItem(reserva.getEstado());
    }
    
    private void guardarReserva() {
        // Validar campos
        if (cboHuesped.getSelectedItem() == null || 
            cboHabitacion.getSelectedItem() == null || 
            fechaInicio.getDate() == null || 
            fechaFin.getDate() == null) {
            
            JOptionPane.showMessageDialog(
                    this, 
                    "Debe completar todos los campos obligatorios", 
                    "Validación", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validar fechas
        Date inicio = fechaInicio.getDate();
        Date fin = fechaFin.getDate();
        
        if (inicio.after(fin)) {
            JOptionPane.showMessageDialog(
                    this, 
                    "La fecha de inicio no puede ser posterior a la fecha de fin", 
                    "Validación", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Obtener datos seleccionados
        Huesped huespedSeleccionado = (Huesped) cboHuesped.getSelectedItem();
        Habitacion habitacionSeleccionada = (Habitacion) cboHabitacion.getSelectedItem();
        String estadoSeleccionado = (String) cboEstado.getSelectedItem();
        
        // Verificar disponibilidad (solo para nuevas reservas o cambios de habitación/fechas)
        if (!modoEdicion || 
            reserva.getIdHabitacion() != habitacionSeleccionada.getIdHabitacion() ||
            !reserva.getFechaInicio().equals(inicio) ||
            !reserva.getFechaFin().equals(fin)) {
            
            boolean disponible = reservaDAO.checkAvailability(
                    habitacionSeleccionada.getIdHabitacion(), inicio, fin);
            
            if (!disponible && !estadoSeleccionado.equals("Cancelada")) {
                JOptionPane.showMessageDialog(
                        this, 
                        "La habitación no está disponible para las fechas seleccionadas", 
                        "Validación", 
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        
        // Establecer datos en el objeto reserva
        reserva.setIdHuesped(huespedSeleccionado.getIdHuesped());
        reserva.setIdHabitacion(habitacionSeleccionada.getIdHabitacion());
        reserva.setFechaInicio(inicio);
        reserva.setFechaFin(fin);
        reserva.setEstado(estadoSeleccionado);
        
        boolean resultado;
        
        if (modoEdicion) {
            resultado = reservaDAO.update(reserva);
        } else {
            resultado = reservaDAO.insert(reserva);
        }
        
        if (resultado) {
            JOptionPane.showMessageDialog(
                    this, 
                    "Reserva guardada correctamente", 
                    "Guardar reserva", 
                    JOptionPane.INFORMATION_MESSAGE);
            
            aceptado = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                    this, 
                    "Error al guardar la reserva", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isAceptado() {
        return aceptado;
    }
}