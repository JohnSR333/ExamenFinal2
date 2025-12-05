package cine.modelos;

import cine.interfaces.Mostrable;

public class Pelicula implements Mostrable {
    protected String titulo;
    protected String genero;
    protected int duracion; 
    protected String clasificacion; 

    public Pelicula(String titulo, String genero, int duracion, String clasificacion) {
        this.titulo = titulo;
        this.genero = genero;
        this.duracion = duracion;
        this.clasificacion = clasificacion;
    }

    @Override
    public void mostrarInfo() {
        System.out.printf("%s (%s) - %d min - %s%n", titulo, genero, duracion, clasificacion);
    }

    public boolean esApta(int edadCliente) {
        switch (clasificacion) {
            case "ATP": return true;
            case "+13": return edadCliente >= 13;
            case "+18": return edadCliente >= 18;
            default: return true;
        }
    }

    public String getTitulo() { return titulo; }
    public String getClasificacion() { return clasificacion; }
}

