package cine.modelos;

import java.util.List;
import java.util.UUID;

public class Reserva {
    private final String codigo;
    private final Cliente cliente;
    private final Funcion funcion;
    private final List<Asiento> asientos;

    public Reserva(Cliente cliente, Funcion funcion, List<Asiento> asientos) {
        this.codigo = UUID.randomUUID().toString().substring(0,8);
        this.cliente = cliente;
        this.funcion = funcion;
        this.asientos = asientos;
    }

    // constructor para carga (mantener codigo)
    public Reserva(String codigo, Cliente cliente, Funcion funcion, List<Asiento> asientos) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.funcion = funcion;
        this.asientos = asientos;
    }

    public String getCodigo() { return codigo; }
    public Cliente getCliente() { return cliente; }
    public Funcion getFuncion() { return funcion; }
    public List<Asiento> getAsientos() { return asientos; }

    public void mostrarTicket() {
        System.out.println("----- TICKET -----");
        System.out.println("Codigo: " + codigo);
        System.out.println("Cliente: " + cliente.getNombre() + " (" + cliente.getDocumento() + ")");
        System.out.println("Pelicula: " + funcion.getPelicula().getTitulo());
        System.out.print("Asientos: ");
        asientos.forEach(a -> System.out.print(a.getFila()+""+a.getNumero()+" "));
        System.out.println("\n------------------");
    }
}


