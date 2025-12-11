package cine.modelos;

public class PeliculaAnimada extends Pelicula {
    public PeliculaAnimada(String titulo, String genero, int duracion, String clasificacion) {
        super(titulo, genero, duracion, clasificacion);
    }
    public PeliculaAnimada(String id, String titulo, String genero, int duracion, String clasificacion) {
        super(id, titulo, genero, duracion, clasificacion);
    }
}


