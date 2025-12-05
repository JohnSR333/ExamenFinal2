package cine.modelos;

import cine.excepciones.AsientoOcupadoException;

public class Asiento {
    private final char fila;
    private final int numero;
    private boolean disponible = true;

    public Asiento(char fila, int numero) {
        this.fila = fila;
        this.numero = numero;
    }

    public void ocupar() throws AsientoOcupadoException {
        if (!disponible) throw new AsientoOcupadoException("Asiento " + fila + numero + " ya está ocupado.");
        disponible = false;
    }

    public void liberar() { disponible = true; }

    public boolean estaDisponible() { return disponible; }

    public char getFila() { return fila; }
    public int getNumero() { return numero; }

    @Override
    public String toString() {
        return String.format("%c%d %s", fila, numero, disponible ? "(Libre)" : "(Ocupado)");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Asiento)) return false;
        Asiento other = (Asiento) obj;
        return this.fila == other.fila && this.numero == other.numero;
    }

    @Override
    public int hashCode() {
        return Character.hashCode(fila) * 31 + Integer.hashCode(numero);
    }
}

