/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.hotel.view;

/**
 *
 * @author pined
 */

import com.hotel.dao.ReservaDAO;
import com.hotel.model.Reserva;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

public class ReservaView extends JFrame {
    
    private JPanel panelBotones, panelBusqueda, panelTabla;
    private JButton btnNuevo, btnEditar, btnEliminar, btnRefrescar, btnCerrar;
    private JTextField txtBusqueda;
    private JButton btnBuscar;
    private JTable tablaReservas;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollPane;
    
    private ReservaDAO reservaDAO;
    private List<Reserva> listaReservas;
    
    public ReservaView() {
        reservaDAO = new ReservaDAO();
        initComponents();
        configurarVentana();
        configurarEventos();
        cargarDatos();
    }
    
    private void initComponents() {
        // Panel de botones
        panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        btnNuevo = new JButton("Nuevo");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnRefrescar = new JButton("Refrescar");
        btnCerrar = new JButton("Cerrar");
        
        panelBotones.add(btnNuevo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnCerrar);
        
        // Panel de búsqueda
        panelBusqueda = new JPanel();
        panelBusqueda.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        JLabel lblBuscar = new JLabel("Buscar por estado:");
        txtBusqueda = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        
        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBusqueda);
        panelBusqueda.add(btnBuscar);
        
        // Panel de tabla
        panelTabla = new JPanel();
        panelTabla.setLayout(new BorderLayout());
        
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Huésped");
        modeloTabla.addColumn("Habitación");
        modeloTabla.addColumn("Fecha Inicio");
        modeloTabla.addColumn("Fecha Fin");
        modeloTabla.addColumn("Estado");
        
        tablaReservas = new JTable(modeloTabla);
        tablaReservas.getTableHeader().setReorderingAllowed(false);
        tablaReservas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaReservas.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        scrollPane = new JScrollPane(tablaReservas);
        panelTabla.add(scrollPane, BorderLayout.CENTER);
    }
    
    private void configurarVentana() {
        setTitle("Gestión de Reservas");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelBotones, BorderLayout.NORTH);
        getContentPane().add(panelTabla, BorderLayout.CENTER);
        getContentPane().add(panelBusqueda, BorderLayout.SOUTH);
    }
    
    private void configurarEventos() {
        btnNuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirDialogoNuevo();
            }
        });
        
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarReserva();
            }
        });
        
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarReserva();
            }
        });
        
        btnRefrescar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarDatos();
            }
        });
        
        btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarReservas();
            }
        });
        
        tablaReservas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editarReserva();
                }
            }
        });
    }
    
    private void cargarDatos() {
        // Limpiar tabla
        modeloTabla.setRowCount(0);
        
        // Obtener datos
        listaReservas = reservaDAO.getAll();
        
        // Formato para fechas
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        // Llenar tabla
        for (Reserva reserva : listaReservas) {
            Object[] fila = {
                reserva.getIdReserva(),
                reserva.getNombreHuesped(),
                reserva.getTipoHabitacion(),
                sdf.format(reserva.getFechaInicio()),
                sdf.format(reserva.getFechaFin()),
                reserva.getEstado()
            };
            modeloTabla.addRow(fila);
        }
        
        // Actualizar estado de botones
        actualizarEstadoBotones();
    }
    
    private void buscarReservas() {
        String textoBusqueda = txtBusqueda.getText().trim();
        
        if (textoBusqueda.isEmpty()) {
            cargarDatos();
            return;
        }
        
        // Limpiar tabla
        modeloTabla.setRowCount(0);
        
        // Buscar reservas
        listaReservas = reservaDAO.searchByStatus(textoBusqueda);
        
        // Formato para fechas
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        // Llenar tabla
        for (Reserva reserva : listaReservas) {
            Object[] fila = {
                reserva.getIdReserva(),
                reserva.getNombreHuesped(),
                reserva.getTipoHabitacion(),
                sdf.format(reserva.getFechaInicio()),
                sdf.format(reserva.getFechaFin()),
                reserva.getEstado()
            };
            modeloTabla.addRow(fila);
        }
        
        // Actualizar estado de botones
        actualizarEstadoBotones();
    }
    
    private void abrirDialogoNuevo() {
        ReservaDialog dialogo = new ReservaDialog(this, true);
        dialogo.setVisible(true);
        
        if (dialogo.isAceptado()) {
            cargarDatos();
        }
    }
    
    private void editarReserva() {
        int filaSeleccionada = tablaReservas.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar una reserva para editar",
                    "Editar reserva", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int idReserva = (int) tablaReservas.getValueAt(filaSeleccionada, 0);
        
        Reserva reservaSeleccionada = reservaDAO.getById(idReserva);
        
        if (reservaSeleccionada != null) {
            ReservaDialog dialogo = new ReservaDialog(this, true, reservaSeleccionada);
            dialogo.setVisible(true);
            
            if (dialogo.isAceptado()) {
                cargarDatos();
            }
        }
    }
    
    private void eliminarReserva() {
        int filaSeleccionada = tablaReservas.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar una reserva para eliminar",
                    "Eliminar reserva", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int idReserva = (int) tablaReservas.getValueAt(filaSeleccionada, 0);
        String nombreHuesped = (String) tablaReservas.getValueAt(filaSeleccionada, 1);
        
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar la reserva de " + nombreHuesped + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        
        if (opcion == JOptionPane.YES_OPTION) {
            boolean resultado = reservaDAO.delete(idReserva);
            
            if (resultado) {
                JOptionPane.showMessageDialog(this,
                        "Reserva eliminada correctamente",
                        "Eliminar reserva", JOptionPane.INFORMATION_MESSAGE);
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar la reserva. Es posible que tenga pagos asociados.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void actualizarEstadoBotones() {
        boolean hayFilas = tablaReservas.getRowCount() > 0;
        btnEditar.setEnabled(hayFilas);
        btnEliminar.setEnabled(hayFilas);
    }
    
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | 
                IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReservaView.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ReservaView().setVisible(true);
            }
        });
    }
}
/*
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

   */ 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
