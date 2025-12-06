package cine.modelos;

public class PeliculaAnimada extends Pelicula {
    public PeliculaAnimada(String titulo, String genero, int duracion, String clasificacion) {
        super(titulo, genero, duracion, clasificacion);
    }

    @Override
    public void mostrarInfo() {
        System.out.print("[Animada] ");
        super.mostrarInfo();
    }
}

