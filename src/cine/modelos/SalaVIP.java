package cine.modelos;

public class SalaVIP extends Sala {
    public SalaVIP(int numero, int filas, int columnas) {
        super(numero, filas, columnas);
    }

    @Override
    public void mostrarDisponibilidad() {
        System.out.println("Sala VIP #" + numero);
        super.mostrarDisponibilidad();
    }
}

