package cine.modelos;

public class Asiento {
    private final char fila;
    private final int numero;
    private boolean disponible;
    private String reservadoPorDocumento; // opcional: referencia al cliente

    public Asiento(char fila, int numero) {
        this.fila = fila;
        this.numero = numero;
        this.disponible = true;
        this.reservadoPorDocumento = null;
    }

    public char getFila() { return fila; }
    public int getNumero() { return numero; }
    public boolean estaDisponible() { return disponible; }

    public void ocupar(Cliente c) {
        this.disponible = false;
        if (c != null) this.reservadoPorDocumento = c.getDocumento();
    }

    public void ocuparSinCliente() {
        this.disponible = false;
    }

    public void liberar() {
        this.disponible = true;
        this.reservadoPorDocumento = null;
    }

    public String getReservadoPorDocumento() { return reservadoPorDocumento; }

    @Override
    public String toString() {
        return fila + "" + numero + (disponible ? " (Libre)" : " (Ocupado)");
    }
}


