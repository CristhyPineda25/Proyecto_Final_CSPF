/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.hotel.view;

/**
 *
 * @author pined
 */

import com.hotel.dao.PagoDAO;
import com.hotel.model.Pago;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
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

public class PagoView extends JFrame {
    
    private JPanel panelBotones, panelBusqueda, panelTabla;
    private JButton btnNuevo, btnEditar, btnEliminar, btnRefrescar, btnCerrar;
    private JTextField txtBusqueda;
    private JButton btnBuscar;
    private JTable tablaPagos;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollPane;
    
    private PagoDAO pagoDAO;
    private List<Pago> listaPagos;
    
    public PagoView() {
        pagoDAO = new PagoDAO();
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
        
        JLabel lblBuscar = new JLabel("Buscar por reserva ID:");
        txtBusqueda = new JTextField(10);
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
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) {
                    return BigDecimal.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };
        
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Reserva ID");
        modeloTabla.addColumn("Monto");
        modeloTabla.addColumn("Fecha Pago");
        modeloTabla.addColumn("Método Pago");
        modeloTabla.addColumn("Huésped");
        
        tablaPagos = new JTable(modeloTabla);
        tablaPagos.getTableHeader().setReorderingAllowed(false);
        tablaPagos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPagos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        scrollPane = new JScrollPane(tablaPagos);
        panelTabla.add(scrollPane, BorderLayout.CENTER);
    }
    
    private void configurarVentana() {
        setTitle("Gestión de Pagos");
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
                editarPago();
            }
        });
        
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarPago();
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
                buscarPagos();
            }
        });
        
        tablaPagos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editarPago();
                }
            }
        });
    }
    
    private void cargarDatos() {
        // Limpiar tabla
        modeloTabla.setRowCount(0);
        
        // Obtener datos
        listaPagos = pagoDAO.getAll();
        
        // Formato para fechas
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        // Llenar tabla
        for (Pago pago : listaPagos) {
            Object[] fila = {
                pago.getIdPago(),
                pago.getIdReserva(),
                pago.getMonto(),
                sdf.format(pago.getFechaPago()),
                pago.getMetodoPago(),
                pago.getNombreHuesped()
            };
            modeloTabla.addRow(fila);
        }
        
        // Actualizar estado de botones
        actualizarEstadoBotones();
    }
    
    private void buscarPagos() {
        String textoBusqueda = txtBusqueda.getText().trim();
        
        if (textoBusqueda.isEmpty()) {
            cargarDatos();
            return;
        }
        
        try {
            int idReserva = Integer.parseInt(textoBusqueda);
            
            // Limpiar tabla
            modeloTabla.setRowCount(0);
            
            // Buscar pagos por reserva
            listaPagos = pagoDAO.getByReserva(idReserva);
            
            // Formato para fechas
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            
            // Llenar tabla
            for (Pago pago : listaPagos) {
                Object[] fila = {
                    pago.getIdPago(),
                    pago.getIdReserva(),
                    pago.getMonto(),
                    sdf.format(pago.getFechaPago()),
                    pago.getMetodoPago(),
                    pago.getNombreHuesped()
                };
                modeloTabla.addRow(fila);
            }
            
            // Actualizar estado de botones
            actualizarEstadoBotones();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El ID de reserva debe ser un número entero",
                    "Error de formato", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void abrirDialogoNuevo() {
        PagoDialog dialogo = new PagoDialog(this, true);
        dialogo.setVisible(true);
        
        if (dialogo.isAceptado()) {
            cargarDatos();
        }
    }
    
    private void editarPago() {
        int filaSeleccionada = tablaPagos.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un pago para editar",
                    "Editar pago", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int idPago = (int) tablaPagos.getValueAt(filaSeleccionada, 0);
        
        Pago pagoSeleccionado = pagoDAO.getById(idPago);
        
        if (pagoSeleccionado != null) {
            PagoDialog dialogo = new PagoDialog(this, true, pagoSeleccionado);
            dialogo.setVisible(true);
            
            if (dialogo.isAceptado()) {
                cargarDatos();
            }
        }
    }
    
    private void eliminarPago() {
        int filaSeleccionada = tablaPagos.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un pago para eliminar",
                    "Eliminar pago", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int idPago = (int) tablaPagos.getValueAt(filaSeleccionada, 0);
        int idReserva = (int) tablaPagos.getValueAt(filaSeleccionada, 1);
        
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar el pago de la reserva " + idReserva + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        
        if (opcion == JOptionPane.YES_OPTION) {
            boolean resultado = pagoDAO.delete(idPago);
            
            if (resultado) {
                JOptionPane.showMessageDialog(this,
                        "Pago eliminado correctamente",
                        "Eliminar pago", JOptionPane.INFORMATION_MESSAGE);
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar el pago",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void actualizarEstadoBotones() {
        boolean hayFilas = tablaPagos.getRowCount() > 0;
        btnEditar.setEnabled(hayFilas);
        btnEliminar.setEnabled(hayFilas);
    }
    
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | 
                IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PagoView.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PagoView().setVisible(true);
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

