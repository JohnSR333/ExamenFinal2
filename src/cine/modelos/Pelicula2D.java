package cine.modelos;

public class Pelicula2D extends Pelicula {
    public Pelicula2D(String titulo, String genero, int duracion, String clasificacion) {
        super(titulo, genero, duracion, clasificacion);
    }

    @Override
    public void mostrarInfo() {
        System.out.print("[2D] ");
        super.mostrarInfo();
    }
}

