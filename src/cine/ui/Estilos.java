package cine.ui;

import javax.swing.*;
import java.awt.*;

public class Estilos {
    public static final Color AZUL_OSCURO = Color.decode("#1F3B73");
    public static final Color AZUL = Color.decode("#3E5BA9");
    public static final Color CELESTE = Color.decode("#5EA3FF");
    public static final Color BLANCO = Color.decode("#F5F7FA");
    public static final Color GRIS = Color.decode("#DFE5EF");

    public static final Font TITULO = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font SUBTITULO = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font TEXTO = new Font("Segoe UI", Font.PLAIN, 14);

    public static JButton crearBoton(String texto) {
        JButton b = new JButton(texto);
        b.setBackground(AZUL);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(TEXTO);
        b.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return b;
    }

    public static JPanel crearPanelConPadding() {
        JPanel p = new JPanel();
        p.setBackground(BLANCO);
        p.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        return p;
    }
}


