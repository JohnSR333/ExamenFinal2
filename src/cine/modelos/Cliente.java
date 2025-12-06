package cine.modelos;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    protected final String nombre;
    protected final String documento;
    protected final String correo;
    protected final List<Reserva> reservas = new ArrayList<>();

    public Cliente(String nombre, String documento, String correo) {
        this.nombre = nombre;
        this.documento = documento;
        this.correo = correo;
    }

    public Reserva hacerReserva(Funcion funcion, List<Asiento> asientos) {
        Reserva r = new Reserva(this, funcion, asientos);
        reservas.add(r);
        r.confirmar();
        return r;
    }

    public List<Reserva> getReservas() { return reservas; }

    public String getNombre() { return nombre; }
    public String getDocumento() { return documento; }
    public String getCorreo() { return correo; }

    @Override
    public String toString() {
        return nombre + " (" + documento + ")";
    }
}

