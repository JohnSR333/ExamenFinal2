package cine.ui;

import cine.app.Main;
import cine.modelos.Reserva;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelClientes extends JPanel {

    private final JTextField txtDocumento = new JTextField();
    private final JTextArea area = new JTextArea();

    public PanelClientes() {
        setLayout(new BorderLayout());
        setBackground(Estilos.BLANCO);

        // --- Título ---
        JLabel titulo = new JLabel("Historial Clientes & Reservas", SwingConstants.CENTER);
        titulo.setFont(Estilos.SUBTITULO);
        titulo.setForeground(Estilos.AZUL_OSCURO);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        // --- Panel de búsqueda ---
        JPanel top = Estilos.crearPanelConPadding();
        top.setLayout(new BorderLayout(6, 6));

        JLabel lblDoc = new JLabel("Documento cliente:");
        lblDoc.setFont(Estilos.TEXTO);

        JButton btnBuscar = Estilos.crearBoton("Buscar reservas");

        top.add(lblDoc, BorderLayout.WEST);
        top.add(txtDocumento, BorderLayout.CENTER);
        top.add(btnBuscar, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);

        // --- Área de texto ---
        area.setEditable(false);
        area.setBackground(Estilos.GRIS);
        area.setFont(Estilos.TEXTO);

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(scroll, BorderLayout.CENTER);

        // Evento botón
        btnBuscar.addActionListener(e -> buscarReservas());
    }

    private void buscarReservas() {
        String doc = txtDocumento.getText().trim();

        if (doc.isEmpty()) {
            area.setText("Ingrese un documento para buscar.");
            return;
        }

        List<Reserva> lista = Main.cine.reservasPorDocumento(doc);

        if (lista.isEmpty()) {
            area.setText("No se encontraron reservas para el documento: " + doc);
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (Reserva r : lista) {
            sb.append("=== RESERVA ===\n");
            sb.append("Código: ").append(r.getCodigo()).append("\n");
            sb.append("Película: ").append(r.getFuncion().getPelicula().getTitulo()).append("\n");
            sb.append("Sala: ").append(r.getFuncion().getSala().getNumero()).append("\n");
            sb.append("Asientos: ");

            r.getAsientos().forEach(a ->
                    sb.append(a.getFila()).append(a.getNumero()).append(" ")
            );

            sb.append("\n-------------------------\n");
        }

        area.setText(sb.toString());
    }
}



