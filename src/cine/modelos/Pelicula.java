package cine.modelos;

import cine.interfaces.Mostrable;
import java.util.UUID;

public class Pelicula implements Mostrable {
    private final String id;
    protected final String titulo;
    protected final String genero;
    protected final int duracion;
    protected final String clasificacion;

    public Pelicula(String titulo, String genero, int duracion, String clasificacion) {
        this.id = UUID.randomUUID().toString();
        this.titulo = titulo;
        this.genero = genero;
        this.duracion = duracion;
        this.clasificacion = clasificacion;
    }

    // constructor para carga desde persistence (mantener id)
    public Pelicula(String id, String titulo, String genero, int duracion, String clasificacion) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.duracion = duracion;
        this.clasificacion = clasificacion;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getGenero() { return genero; }
    public int getDuracion() { return duracion; }
    public String getClasificacion() { return clasificacion; }

    @Override
    public void mostrarInfo() {
        System.out.printf("%s (%s) - %d min - %s%n", titulo, genero, duracion, clasificacion);
    }

    @Override
    public String toString() {
        return titulo + " - " + genero + " (" + duracion + " min)";
    }
}



