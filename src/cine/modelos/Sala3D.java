package cine.modelos;

public class Sala3D extends Sala {
    public Sala3D(int numero, int filas, int columnas) {
        super(numero, filas, columnas);
    }

    @Override
    public void mostrarDisponibilidad() {
        System.out.println("[Sala 3D] #" + getNumero());
        super.mostrarDisponibilidad();
    }
}

