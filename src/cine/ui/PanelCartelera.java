package cine.ui;

import cine.app.Main;
import cine.modelos.Funcion;

import javax.swing.*;
import java.awt.*;

public class PanelCartelera extends JPanel {

    private final JTextArea area = new JTextArea();

    public PanelCartelera() {
        setLayout(new BorderLayout());
        setBackground(Estilos.BLANCO);

        JLabel titulo = new JLabel("Cartelera", SwingConstants.CENTER);
        titulo.setFont(Estilos.SUBTITULO);
        titulo.setForeground(Estilos.AZUL_OSCURO);
        titulo.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        area.setFont(Estilos.TEXTO);
        area.setEditable(false);
        area.setBackground(Estilos.GRIS);

        JButton btnActualizar = Estilos.crearBoton("Actualizar cartelera");
        btnActualizar.addActionListener(e -> cargarCartelera());

        add(titulo, BorderLayout.NORTH);
        add(new JScrollPane(area), BorderLayout.CENTER);
        add(btnActualizar, BorderLayout.SOUTH);

        cargarCartelera();
    }

    private void cargarCartelera() {
        area.setText("");
        int i = 0;
        for (Funcion f : Main.cine.getFunciones()) {
            area.append("[" + i + "] " + f.toString() + "\n\n");
            i++;
        }
        if (i == 0) area.setText("No hay funciones registradas.");
    }
}


