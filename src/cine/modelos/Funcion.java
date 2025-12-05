package cine.modelos;

import java.time.LocalDate;
import java.time.LocalTime;

public class Funcion {
    private final Pelicula pelicula;
    private final Sala sala;
    private final LocalDate fecha;
    private final LocalTime hora;

    public Funcion(Pelicula pelicula, Sala sala, LocalDate fecha, LocalTime hora) {
        this.pelicula = pelicula;
        this.sala = sala;
        this.fecha = fecha;
        this.hora = hora;
    }

    public void mostrarDetalles() {
        System.out.println("--------------------------------------------------");
        System.out.printf("%s | Fecha: %s | Hora: %s | Sala: %d%n",
                pelicula.getTitulo(), fecha.toString(), hora.toString(), sala.getNumero());
        pelicula.mostrarInfo();
        System.out.println("--------------------------------------------------");
    }

    public Sala getSala() { return sala; }
    public Pelicula getPelicula() { return pelicula; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHora() { return hora; }

    @Override
    public String toString() {
        return String.format("%s - %s %s - Sala %d",
                pelicula.getTitulo(), fecha.toString(), hora.toString(), sala.getNumero());
    }
}

