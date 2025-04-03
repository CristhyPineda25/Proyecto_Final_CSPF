/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.hotel.view;

/**
 *
 * @author pined
 */
import com.hotel.model.Usuario;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainView extends JFrame {
    
    private JMenuBar menuBar;
    private JMenu menuArchivo, menuGestion, menuReportes, menuAyuda;
    private JMenuItem menuItemSalir, menuItemHuespedes, menuItemHabitaciones, 
                      menuItemReservas, menuItemPagos, menuItemAcercaDe;
    private JToolBar toolBar;
    private JPanel statusBar;
    private JLabel lblEstado;
    private JLabel lblImagenCentro;
    
    private Usuario usuarioActual;
     
    public MainView(Usuario usuario) {
        this.usuarioActual = usuario;
        initComponents();
        configurarVentana();
        configurarEventos();
    }
    
    private void initComponents() {
        // Barra de menú
        menuBar = new JMenuBar();
        
        // Menú Archivo
        menuArchivo = new JMenu("Archivo");
        menuItemSalir = new JMenuItem("Salir");
        menuArchivo.add(menuItemSalir);
        
        // Menú Gestión
        menuGestion = new JMenu("Gestión");
        menuItemHuespedes = new JMenuItem("Huéspedes");
        menuItemHabitaciones = new JMenuItem("Habitaciones");
        menuItemReservas = new JMenuItem("Reservas");
        menuItemPagos = new JMenuItem("Pagos");
        menuGestion.add(menuItemHuespedes);
        menuGestion.add(menuItemHabitaciones);
        menuGestion.add(menuItemReservas);
        menuGestion.add(menuItemPagos);
        
        // Menú Ayuda
        menuAyuda = new JMenu("Ayuda");
        menuItemAcercaDe = new JMenuItem("Acerca de");
        menuAyuda.add(menuItemAcercaDe);
        
        // Agregar menús a la barra
        menuBar.add(menuArchivo);
        menuBar.add(menuGestion);
        menuBar.add(menuAyuda);
        
        // Barra de herramientas
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        // Barra de estado
        statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblEstado = new JLabel("Usuario: " + usuarioActual.getUsuario() + " | Rol: " + usuarioActual.getNombreRol());
        statusBar.add(lblEstado);
        
        // Panel central con imagen
        JPanel panelCentral = new JPanel(new BorderLayout());
        
        try {
            // Cargar la imagen desde la raíz del proyecto
            ImageIcon iconoOriginal = new ImageIcon("Hotel.png");
            
            // Redimensionar la imagen
            Image imagen = iconoOriginal.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
            ImageIcon icono = new ImageIcon(imagen);
            
            lblImagenCentro = new JLabel(icono);
            lblImagenCentro.setHorizontalAlignment(JLabel.CENTER);
            panelCentral.add(lblImagenCentro, BorderLayout.CENTER);
            
        } catch (Exception e) {
            lblImagenCentro = new JLabel("Imagen no encontrada");
            lblImagenCentro.setHorizontalAlignment(JLabel.CENTER);
            panelCentral.add(lblImagenCentro, BorderLayout.CENTER);
            System.err.println("Error al cargar la imagen: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Configuración principal de la ventana
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(toolBar, BorderLayout.NORTH);
        getContentPane().add(panelCentral, BorderLayout.CENTER);
        getContentPane().add(statusBar, BorderLayout.SOUTH);
    }
    
    private void configurarVentana() {
        setTitle("Sistema de Gestión Hotelera");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setJMenuBar(menuBar);
        
        // Configurar icono de ventana
        try {
            ImageIcon icon = new ImageIcon("Hotel.png");
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.err.println("Error al cargar el icono: " + e.getMessage());
        }
    }
    
    private void configurarEventos() {
        // Evento de cierre de ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmarSalida();
            }
        });
        
        // Evento del menú Salir
        menuItemSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmarSalida();
            }
        });
        
        // Evento del menú Huéspedes
        menuItemHuespedes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirGestionHuespedes();
            }
        });
        
        // Evento del menú Habitaciones
        menuItemHabitaciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirGestionHabitaciones();
            }
        });
        
        // Evento del menú Reservas
        menuItemReservas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirGestionReservas();
            }
        });
        
        // Evento del menú Pagos
        menuItemPagos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirGestionPagos();
            }
        });
        
        // Evento del menú Acerca de
        menuItemAcercaDe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainView.this,
                        "Sistema de Gestión Hotelera\nVersión 1.0\n© 2025",
                        "Acerca de", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
    
    private void confirmarSalida() {
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea salir de la aplicación?",
                "Confirmar salida", JOptionPane.YES_NO_OPTION);
        
        if (opcion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    private void abrirGestionHuespedes() {
        HuespedView view = new HuespedView();
        view.setVisible(true);
    }
    
    private void abrirGestionHabitaciones() {
        HabitacionView view = new HabitacionView();
        view.setVisible(true);
    }
    
    private void abrirGestionReservas() {
        ReservaView view = new ReservaView();
        view.setVisible(true);
    }
    
    private void abrirGestionPagos() {
        PagoView view = new PagoView();
        view.setVisible(true);
    }
    
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | 
                IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainView.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Normalmente, esto no se usaría directamente, sino a través del login
                Usuario demoUsuario = new Usuario();
                demoUsuario.setUsuario("admin");
                demoUsuario.setNombreRol("Administrador");
                
                new MainView(demoUsuario).setVisible(true);
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

