package cine.modelos;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cliente {
    private final String id;
    protected final String nombre;
    protected final String documento;
    protected final String correo;
    protected final List<Reserva> reservas = new ArrayList<>();

    public Cliente(String nombre, String documento, String correo) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.documento = documento;
        this.correo = correo;
    }

    public Cliente(String id, String nombre, String documento, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.documento = documento;
        this.correo = correo;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDocumento() { return documento; }
    public String getCorreo() { return correo; }

    public Reserva hacerReserva(Funcion f, List<Asiento> asientos) {
        // ocupar asientos y marcar cliente
        for (Asiento a : asientos) a.ocupar(this);
        Reserva r = new Reserva(this, f, new ArrayList<>(asientos));
        reservas.add(r);
        // agregar reserva a la función también
        f.agregarReserva(r);
        return r;
    }

    public List<Reserva> getReservas() { return reservas; }

    @Override
    public String toString() {
        return nombre + " (" + documento + ")";
    }
}


