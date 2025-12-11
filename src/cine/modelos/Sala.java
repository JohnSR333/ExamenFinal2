package cine.modelos;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Sala {
    private final String id;
    private final int numero;
    private final int filas;
    private final int columnas;
    private final Asiento[][] asientos;

    public Sala(int numero, int filas, int columnas) {
        this.id = UUID.randomUUID().toString();
        this.numero = numero;
        this.filas = filas;
        this.columnas = columnas;
        this.asientos = new Asiento[filas][columnas];
        for (int r = 0; r < filas; r++) {
            char letra = (char) ('A' + r);
            for (int c = 0; c < columnas; c++) {
                asientos[r][c] = new Asiento(letra, c + 1);
            }
        }
    }

    // constructor para carga
    public Sala(String id, int numero, int filas, int columnas) {
        this.id = id;
        this.numero = numero;
        this.filas = filas;
        this.columnas = columnas;
        this.asientos = new Asiento[filas][columnas];
        for (int r = 0; r < filas; r++) {
            char letra = (char) ('A' + r);
            for (int c = 0; c < columnas; c++) {
                asientos[r][c] = new Asiento(letra, c + 1);
            }
        }
    }

    public String getId() { return id; }
    public int getNumero() { return numero; }
    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }
    public Asiento[][] getAsientos() { return asientos; }

    public List<Asiento> getAsientosList() {
        List<Asiento> list = new ArrayList<>();
        for (int r = 0; r < filas; r++) for (int c = 0; c < columnas; c++) list.add(asientos[r][c]);
        return list;
    }

    public Asiento buscarAsiento(char fila, int numero) {
        int row = fila - 'A';
        int col = numero - 1;
        if (row < 0 || row >= filas || col < 0 || col >= columnas) return null;
        return asientos[row][col];
    }
}


