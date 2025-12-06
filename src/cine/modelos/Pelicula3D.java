package cine.modelos;

public class Pelicula3D extends Pelicula {
    public Pelicula3D(String titulo, String genero, int duracion, String clasificacion) {
        super(titulo, genero, duracion, clasificacion);
    }

    @Override
    public void mostrarInfo() {
        System.out.print("[3D] ");
        super.mostrarInfo();
    }
}

