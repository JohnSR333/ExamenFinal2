package cine.modelos;

public class ClienteVIP extends Cliente {
    private final int nivel; 

    public ClienteVIP(String nombre, String documento, String correo, int nivel) {
        super(nombre, documento, correo);
        this.nivel = nivel;
    }

    public int getNivel() { return nivel; }

    @Override
    public String toString() {
        return super.toString() + " [VIP nivel " + nivel + "]";
    }
}

