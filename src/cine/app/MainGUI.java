package cine.app;

import cine.ui.VentanaPrincipal;

import javax.swing.SwingUtilities;

/**
 * Launcher para la interfaz Swing.
 * Ejecuta MainGUI.main(...) para abrir la ventana.
 */
public class MainGUI {
    public static void main(String[] args) {
        // inicializar datos de ejemplo
        Main.inicializarDatosPublico();

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal vp = new VentanaPrincipal();
            vp.setVisible(true);
        });

    }
}
