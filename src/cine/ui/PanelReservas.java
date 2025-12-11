package cine.ui;

import cine.app.Main;
import cine.modelos.*;
import cine.sistema.PersistenciaJson;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel bonito para reservar asientos.
 */
public class PanelReservas extends JPanel {

    private final JTextField campoIndice = new JTextField();
    private final JTextField campoFila = new JTextField();
    private final JTextField campoNumero = new JTextField();
    private final JTextArea salida = new JTextArea();

    public PanelReservas() {
        setLayout(new BorderLayout());
        setBackground(Estilos.BLANCO);

        JLabel titulo = new JLabel("Reservas", SwingConstants.CENTER);
        titulo.setFont(Estilos.SUBTITULO);
        titulo.setForeground(Estilos.AZUL_OSCURO);
        titulo.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel form = Estilos.crearPanelConPadding();
        form.setLayout(new GridLayout(5,2,8,8));

        form.add(new JLabel("Índice función:"));
        form.add(campoIndice);

        form.add(new JLabel("Fila (A-Z):"));
        form.add(campoFila);

        form.add(new JLabel("Número:"));
        form.add(campoNumero);

        JButton btnVer = Estilos.crearBoton("Ver asiento");
        JButton btnReservar = Estilos.crearBoton("Reservar asiento");

        form.add(btnVer);
        form.add(btnReservar);

        salida.setEditable(false);
        salida.setFont(Estilos.TEXTO);
        salida.setBackground(Estilos.GRIS);

        btnVer.addActionListener(e -> verAsiento());
        btnReservar.addActionListener(e -> reservarAsiento());

        add(titulo, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        add(new JScrollPane(salida), BorderLayout.SOUTH);
    }

    private void verAsiento() {
        try {
            int idx = Integer.parseInt(campoIndice.getText().trim());
            char fila = campoFila.getText().toUpperCase().charAt(0);
            int num = Integer.parseInt(campoNumero.getText().trim());

            Funcion f = Main.cine.buscarFuncionPorIndice(idx).orElse(null);
            if (f == null) { salida.setText("Función inválida."); return; }

            Asiento a = f.getSala().buscarAsiento(fila, num);
            if (a == null) { salida.setText("Asiento inválido."); return; }

            salida.setText("Asiento " + a.getFila() + a.getNumero() + " -> " + (a.estaDisponible() ? "Libre" : "Ocupado"));
        } catch (Exception ex) {
            salida.setText("Error: rellena los campos correctamente.");
        }
    }

    private void reservarAsiento() {
        try {
            int idx = Integer.parseInt(campoIndice.getText().trim());
            char fila = campoFila.getText().toUpperCase().charAt(0);
            int num = Integer.parseInt(campoNumero.getText().trim());

            Funcion f = Main.cine.buscarFuncionPorIndice(idx).orElse(null);
            if (f == null) { salida.setText("Función inválida."); return; }

            Asiento a = f.getSala().buscarAsiento(fila, num);
            if (a == null) { salida.setText("Asiento inválido."); return; }
            if (!a.estaDisponible()) { salida.setText("Asiento ya ocupado."); return; }

            // Pedir datos del cliente con dialogo simple
            String nombre = JOptionPane.showInputDialog(this, "Nombre del cliente:");
            if (nombre == null || nombre.trim().isEmpty()) { salida.setText("Reserva cancelada: nombre requerido."); return; }
            String documento = JOptionPane.showInputDialog(this, "Documento:");
            if (documento == null || documento.trim().isEmpty()) { salida.setText("Reserva cancelada: documento requerido."); return; }
            String correo = JOptionPane.showInputDialog(this, "Correo (opcional):");

            String vip = JOptionPane.showInputDialog(this, "¿VIP? (s/n):", "n");
            Cliente cliente = "s".equalsIgnoreCase(vip) ? new ClienteVIP(nombre, documento, correo, 1) : new ClienteRegular(nombre, documento, correo);

            // registrar cliente en cine (si no existe)
            Main.cine.registrarCliente(cliente);

            List<Asiento> seleccion = new ArrayList<>();
            seleccion.add(a);

            // crear reserva (esto ocupará el asiento y la añadirá a cliente y funcion)
            Reserva r = cliente.hacerReserva(f, seleccion);

            // registrar reserva en el cine (para búsquedas y persistencia)
            Main.cine.registrarReserva(r);

            // guardar inmediatamente en JSON
            PersistenciaJson.guardar(Main.cine);

            salida.setText("Reserva realizada. Código: " + r.getCodigo());
        } catch (Exception ex) {
            salida.setText("Error procesando reserva: " + ex.getMessage());
        }
    }
}


