package cine.ui;

import cine.app.Main;
import cine.modelos.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel para visualizar los asientos de una función (matriz),
 * reservar haciendo click sobre asientos libres y reiniciar la función.
 *
 * Diseño: botones verdes (libre) / rojos (ocupado).
 */
public class PanelAsientos extends JPanel {

    private final JComboBox<Funcion> cbFunciones = new JComboBox<>();
    private final JPanel gridPanel = new JPanel();
    private final JButton btnReiniciar = Estilos.crearBoton("Reiniciar función");
    private final JButton btnRefrescar = Estilos.crearBoton("Refrescar");
    private final List<JButton> seatButtons = new ArrayList<>();
    private Funcion funcionActual;

    public PanelAsientos() {
        setLayout(new BorderLayout());
        setBackground(Estilos.BLANCO);

        JLabel titulo = new JLabel("Asientos (visualización y reserva)", SwingConstants.CENTER);
        titulo.setFont(Estilos.SUBTITULO);
        titulo.setForeground(Estilos.AZUL_OSCURO);
        titulo.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        add(titulo, BorderLayout.NORTH);

        // panel top con selector y botones
        JPanel top = Estilos.crearPanelConPadding();
        top.setLayout(new BorderLayout(8,8));

        cbFunciones.setPreferredSize(new Dimension(400, 28));
        top.add(cbFunciones, BorderLayout.CENTER);

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        topRight.setBackground(Estilos.BLANCO);
        topRight.add(btnRefrescar);
        topRight.add(btnReiniciar);
        top.add(topRight, BorderLayout.EAST);

        add(top, BorderLayout.PAGE_START);

        // grid holder
        gridPanel.setBackground(Estilos.BLANCO);
        JScrollPane sp = new JScrollPane(gridPanel);
        sp.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        add(sp, BorderLayout.CENTER);

        // acciones
        btnRefrescar.addActionListener(e -> construirGrid());
        btnReiniciar.addActionListener(e -> reiniciarFuncionSeleccionada());
        cbFunciones.addActionListener(e -> construirGrid());

        reloadFunciones();
    }

    public void reloadFunciones() {
        cbFunciones.removeAllItems();
        for (Funcion f : Main.cine.getFunciones()) cbFunciones.addItem(f);
        if (cbFunciones.getItemCount() > 0) cbFunciones.setSelectedIndex(0);
        construirGrid();
    }

    private void construirGrid() {
        Funcion f = (Funcion) cbFunciones.getSelectedItem();
        funcionActual = f;
        gridPanel.removeAll();
        seatButtons.clear();

        if (f == null) {
            gridPanel.setLayout(new BorderLayout());
            gridPanel.add(new JLabel("No hay funciones."), BorderLayout.CENTER);
            revalidate();
            repaint();
            return;
        }

        Sala sala = f.getSala();
        int filas = sala.getFilas();
        int cols = sala.getColumnas();

        gridPanel.setLayout(new GridLayout(filas, cols, 6, 6));

        for (int r = 0; r < filas; r++) {
            for (int c = 0; c < cols; c++) {
                Asiento a = sala.getAsientos()[r][c];
                String text = "" + a.getFila() + a.getNumero();
                JButton b = new JButton(text);
                b.setOpaque(true);
                b.setBorderPainted(false);
                b.setFont(Estilos.TEXTO);

                updateButtonStyle(b, a);

                // acción: si está libre, al hacer click reserva mediante diálogo
                b.addActionListener(evt -> {
                    if (!a.estaDisponible()) {
                        JOptionPane.showMessageDialog(this, "Asiento ocupado.");
                        return;
                    }
                    // pedir datos y crear reserva
                    String nombre = JOptionPane.showInputDialog(this, "Nombre del cliente:");
                    if (nombre == null || nombre.trim().isEmpty()) return;
                    String documento = JOptionPane.showInputDialog(this, "Documento:");
                    if (documento == null || documento.trim().isEmpty()) return;
                    String correo = JOptionPane.showInputDialog(this, "Correo (opcional):", "");
                    String vip = JOptionPane.showInputDialog(this, "¿VIP? (s/n):", "n");

                    Cliente cliente = "s".equalsIgnoreCase(vip) ? new ClienteVIP(nombre, documento, correo, 1)
                                                              : new ClienteRegular(nombre, documento, correo);

                    // registrar cliente (no duplica)
                    Main.cine.registrarCliente(cliente);

                    // crear reserva y registrar
                    List<Asiento> seleccion = new ArrayList<>();
                    seleccion.add(a);
                    Reserva rsv = cliente.hacerReserva(f, seleccion);
                    Main.cine.registrarReserva(rsv);

                    // guardar persistencia
                    cine.sistema.PersistenciaJson.guardar(Main.cine);

                    // actualizar botón
                    updateButtonStyle(b, a);
                    JOptionPane.showMessageDialog(this, "Reserva realizada. Código: " + rsv.getCodigo());
                });

                seatButtons.add(b);
                gridPanel.add(b);
            }
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void updateButtonStyle(JButton b, Asiento a) {
        if (!a.estaDisponible()) {
            b.setBackground(new Color(210, 66, 66)); // rojo
            b.setForeground(Color.WHITE);
            b.setEnabled(false);
        } else {
            b.setBackground(new Color(34, 197, 94)); // verde
            b.setForeground(Color.WHITE);
            b.setEnabled(true);
        }
    }

    private void reiniciarFuncionSeleccionada() {
        if (funcionActual == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una función primero.");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres reiniciar (liberar) todos los asientos de esta función?\n" +
                        "Esto eliminará las reservas asociadas a la función.",
                "Confirmar reinicio", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;

        Main.cine.reiniciarFuncion(funcionActual);
        // guardar persistencia
        cine.sistema.PersistenciaJson.guardar(Main.cine);
        // reconstruir grid
        construirGrid();
        JOptionPane.showMessageDialog(this, "Función reiniciada correctamente.");
    }
}

