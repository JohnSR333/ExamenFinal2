package cine.modelos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Sala {
    protected final int numero;
    protected final List<Asiento> asientos;

    public Sala(int numero, int filas, int columnas) {
        this.numero = numero;
        this.asientos = new ArrayList<>();
        for (int f = 0; f < filas; f++) {
            char fila = (char) ('A' + f);
            for (int c = 1; c <= columnas; c++) {
                asientos.add(new Asiento(fila, c));
            }
        }
    }

    public List<Asiento> getAsientos() { return asientos; }

    public void mostrarDisponibilidad() {
        System.out.println("Disponibilidad Sala #" + numero);
        String salida = asientos.stream()
                .map(a -> a.toString())
                .collect(Collectors.joining(" | "));
        System.out.println(salida);
    }

    public Asiento buscarAsiento(char fila, int numero) {
        return asientos.stream()
                .filter(a -> a.getFila() == fila && a.getNumero() == numero)
                .findFirst()
                .orElse(null);
    }

    public int getNumero() { return numero; }

    @Override
    public String toString() {
        return "Sala#" + numero;
    }
}

