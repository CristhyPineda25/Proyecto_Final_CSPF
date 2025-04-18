/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

public class HuespedView extends JFrame {
    
    private JPanel panelBotones, panelBusqueda, panelTabla;
    private JButton btnNuevo, btnEditar, btnEliminar, btnRefrescar, btnCerrar;
    private JTextField txtBusqueda;
    private JButton btnBuscar;
    private JTable tablaHuespedes;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollPane;
    
    private HuespedDAO huespedDAO;
    private List<Huesped> listaHuespedes;
    
    public HuespedView() {
        huespedDAO = new HuespedDAO();
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
        
        JLabel lblBuscar = new JLabel("Buscar:");
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
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Apellido");
        modeloTabla.addColumn("Email");
        modeloTabla.addColumn("Teléfono");
        modeloTabla.addColumn("Dirección");
        
        tablaHuespedes = new JTable(modeloTabla);
        tablaHuespedes.getTableHeader().setReorderingAllowed(false);
        tablaHuespedes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaHuespedes.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        scrollPane = new JScrollPane(tablaHuespedes);
        panelTabla.add(scrollPane, BorderLayout.CENTER);
    }
    
    private void configurarVentana() {
        setTitle("Gestión de Huéspedes");
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
                editarHuesped();
            }
        });
        
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarHuesped();
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
                buscarHuespedes();
            }
        });
        
        tablaHuespedes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editarHuesped();
                }
            }
        });
    }
    
    private void cargarDatos() {
        // Limpiar tabla
        modeloTabla.setRowCount(0);
        
        // Obtener datos
        listaHuespedes = huespedDAO.getAll();
        
        // Llenar tabla
        for (Huesped huesped : listaHuespedes) {
            Object[] fila = {
                huesped.getIdHuesped(),
                huesped.getNombre(),
                huesped.getApellido(),
                huesped.getEmail(),
                huesped.getTelefono(),
                huesped.getDireccion()
            };
            modeloTabla.addRow(fila);
        }
        
        // Actualizar estado de botones
        actualizarEstadoBotones();
    }
    
    private void buscarHuespedes() {
        String textoBusqueda = txtBusqueda.getText().trim();
        
        if (textoBusqueda.isEmpty()) {
            cargarDatos();
            return;
        }
        
        // Limpiar tabla
        modeloTabla.setRowCount(0);
        
        // Buscar huéspedes
        listaHuespedes = huespedDAO.searchByName(textoBusqueda);
        
        // Llenar tabla
        for (Huesped huesped : listaHuespedes) {
            Object[] fila = {
                huesped.getIdHuesped(),
                huesped.getNombre(),
                huesped.getApellido(),
                huesped.getEmail(),
                huesped.getTelefono(),
                huesped.getDireccion()
            };
            modeloTabla.addRow(fila);
        }
        
        // Actualizar estado de botones
        actualizarEstadoBotones();
    }
    
    private void abrirDialogoNuevo() {
        HuespedDialog dialogo = new HuespedDialog(this, true);
        dialogo.setVisible(true);
        
        if (dialogo.isAceptado()) {
            cargarDatos();
        }
    }
    
    private void editarHuesped() {
        int filaSeleccionada = tablaHuespedes.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un huésped para editar",
                    "Editar huésped", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int idHuesped = (int) tablaHuespedes.getValueAt(filaSeleccionada, 0);
        
        Huesped huespedSeleccionado = huespedDAO.getById(idHuesped);
        
        if (huespedSeleccionado != null) {
            HuespedDialog dialogo = new HuespedDialog(this, true, huespedSeleccionado);
            dialogo.setVisible(true);
            
            if (dialogo.isAceptado()) {
                cargarDatos();
            }
        }
    }
    
    private void eliminarHuesped() {
        int filaSeleccionada = tablaHuespedes.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un huésped para eliminar",
                    "Eliminar huésped", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int idHuesped = (int) tablaHuespedes.getValueAt(filaSeleccionada, 0);
        String nombreHuesped = tablaHuespedes.getValueAt(filaSeleccionada, 1) + " " +
                              tablaHuespedes.getValueAt(filaSeleccionada, 2);
        
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar al huésped " + nombreHuesped + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        
        if (opcion == JOptionPane.YES_OPTION) {
            boolean resultado = huespedDAO.delete(idHuesped);
            
            if (resultado) {
                JOptionPane.showMessageDialog(this,
                        "Huésped eliminado correctamente",
                        "Eliminar huésped", JOptionPane.INFORMATION_MESSAGE);
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar el huésped. Es posible que tenga reservas asociadas.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void actualizarEstadoBotones() {
        boolean hayFilas = tablaHuespedes.getRowCount() > 0;
        btnEditar.setEnabled(hayFilas);
        btnEliminar.setEnabled(hayFilas);
    }
    
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | 
                IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HuespedView.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new HuespedView().setVisible(true);
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
