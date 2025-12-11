package cine.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal bonita
 */
public class VentanaPrincipal extends JFrame {

private final PanelCartelera panelCartelera = new PanelCartelera();
private final PanelReservas panelReservas = new PanelReservas();
private final PanelClientes panelClientes = new PanelClientes();
private final PanelAsientos panelAsientos = new PanelAsientos();

    public VentanaPrincipal() {
        setTitle("Cine - Sistema de Reservas");
        setSize(980, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        getContentPane().setBackground(Estilos.AZUL_OSCURO);

        JLabel titulo = new JLabel("Sistema de Gestión de Cine", SwingConstants.CENTER);
        titulo.setFont(Estilos.TITULO);
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(18, 10, 18, 10));
        add(titulo, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(Estilos.SUBTITULO);
        tabs.setBackground(Estilos.AZUL_OSCURO);

        tabs.addTab("Cartelera", panelCartelera);
        tabs.addTab("Reservas", panelReservas);
        tabs.addTab("Clientes", panelClientes);
        tabs.addTab("Asientos", panelAsientos); 
        
        add(tabs, BorderLayout.CENTER);

        // save on window close
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                // save data before exit
                cine.sistema.PersistenciaJson.guardar(cine.app.Main.cine);
                super.windowClosing(e);
            }
        });
    }
}


