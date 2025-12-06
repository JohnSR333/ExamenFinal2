package cine.modelos;

import cine.excepciones.AsientoOcupadoException;

import java.util.List;

public class Reserva {
    private final Cliente cliente;
    private final Funcion funcion;
    private final List<Asiento> asientos;

    public Reserva(Cliente cliente, Funcion funcion, List<Asiento> asientos) {
        this.cliente = cliente;
        this.funcion = funcion;
        this.asientos = asientos;
    }

    public void confirmar() {
        for (Asiento a : asientos) {
            try {
                a.ocupar();
            } catch (AsientoOcupadoException e) {
                System.out.println("Error al confirmar: " + e.getMessage());
            }
        }
    }

    public void cancelar() {
        for (Asiento a : asientos) a.liberar();
    }

    public void mostrarTicket() {
        System.out.println("=============== TICKET ===============");
        System.out.println("Cliente: " + cliente);
        System.out.println("Película: " + funcion.getPelicula().getTitulo());
        System.out.println("Fecha: " + funcion.getFecha() + " Hora: " + funcion.getHora());
        System.out.print("Asientos: ");
        asientos.forEach(a -> System.out.print(a.getFila() + "" + a.getNumero() + " "));
        System.out.println("\n======================================");
    }

    public Cliente getCliente() { return cliente; }
    public Funcion getFuncion() { return funcion; }
    public List<Asiento> getAsientos() { return asientos; }
}

