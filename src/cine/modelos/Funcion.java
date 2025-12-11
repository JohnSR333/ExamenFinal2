package cine.modelos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Funcion {
    private final String id;
    private final Pelicula pelicula;
    private final Sala sala;
    private final LocalDate fecha;
    private final LocalTime hora;

    private final List<Reserva> reservas = new ArrayList<>();

    public Funcion(Pelicula pelicula, Sala sala, LocalDate fecha, LocalTime hora) {
        this.id = UUID.randomUUID().toString();
        this.pelicula = pelicula;
        this.sala = sala;
        this.fecha = fecha;
        this.hora = hora;
    }

    // constructor para carga con id
    public Funcion(String id, Pelicula pelicula, Sala sala, LocalDate fecha, LocalTime hora) {
        this.id = id;
        this.pelicula = pelicula;
        this.sala = sala;
        this.fecha = fecha;
        this.hora = hora;
    }

    public String getId() { return id; }
    public Pelicula getPelicula() { return pelicula; }
    public Sala getSala() { return sala; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHora() { return hora; }

    public List<Reserva> getReservas() { return reservas; }
    public void agregarReserva(Reserva r) { reservas.add(r); }

    @Override
    public String toString() {
        return pelicula.getTitulo() + " | Sala " + sala.getNumero() + " | " + fecha + " " + hora;
    }
}


