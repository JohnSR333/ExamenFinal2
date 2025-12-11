package cine.sistema;

import cine.modelos.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Guardado {

    private static final String FILE = "reservas.txt";

    // Guardar reservas en archivo
    public static void guardarReservas(List<Reserva> reservas) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            for (Reserva r : reservas) {
                pw.println(r.getCodigo() + "|" +
                        r.getCliente().getDocumento() + "|" +
                        r.getFuncion().getPelicula().getTitulo() + "|" +
                        r.getFuncion().getSala().getNumero() + "|" +
                        asientosToString(r.getAsientos()));
            }
        } catch (Exception e) {
            System.out.println("Error guardando reservas: " + e.getMessage());
        }
    }

    // Convertir asientos a texto fila-numero;fila-numero
    private static String asientosToString(List<Asiento> lista) {
        StringBuilder sb = new StringBuilder();
        for (Asiento a : lista) {
            sb.append(a.getFila()).append("-").append(a.getNumero()).append(";");
        }
        return sb.toString();
    }

    // Cargar reservas del archivo
    public static List<String> cargarArchivo() {
        List<String> lineas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (Exception ignored) {}
        return lineas;
    }
}

