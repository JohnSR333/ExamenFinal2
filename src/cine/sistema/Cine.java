package cine.sistema;

import cine.modelos.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Cine {

    private final List<Pelicula> peliculas = new ArrayList<>();
    private final List<Sala> salas = new ArrayList<>();
    private final List<Funcion> funciones = new ArrayList<>();
    private final List<Cliente> clientes = new ArrayList<>();

    private final List<Reserva> reservas = new ArrayList<>();

    public Cine() {
        cargarReservasDesdeArchivo();
    }

    // --- PELICULAS, SALAS, FUNCIONES ---

    public void agregarPelicula(Pelicula p) { peliculas.add(p); }
    public void agregarSala(Sala s) { salas.add(s); }
    public void programarFuncion(Funcion f) { funciones.add(f); }

    public List<Funcion> getFunciones() { return funciones; }
    public List<Pelicula> getPeliculas() { return peliculas; }
    public List<Sala> getSalas() { return salas; }

    public Optional<Funcion> buscarFuncionPorIndice(int idx) {
        if (idx < 0 || idx >= funciones.size()) return Optional.empty();
        return Optional.of(funciones.get(idx));
    }

    // --- CLIENTES ---

    public void registrarCliente(Cliente c) {
        boolean existe = clientes.stream()
                .anyMatch(x -> x.getDocumento().equalsIgnoreCase(c.getDocumento()));
        if (!existe) clientes.add(c);
    }

    public List<Cliente> getClientes() { return clientes; }

    // --- RESERVAS ---

    public void registrarReserva(Reserva r) {
        reservas.add(r);
        Guardado.guardarReservas(reservas);
    }

    public List<Reserva> reservasPorDocumento(String documento) {
        return reservas.stream()
                .filter(r -> r.getCliente().getDocumento().equalsIgnoreCase(documento))
                .collect(Collectors.toList());
    }

    public List<Reserva> getReservas() { return reservas; }

    public void setPeliculas(List<Pelicula> list) {
        peliculas.clear();
        peliculas.addAll(list);
    }

    public void setSalas(List<Sala> list) {
        salas.clear();
        salas.addAll(list);
    }

    public void setFunciones(List<Funcion> list) {
        funciones.clear();
        funciones.addAll(list);
    }

    public void setClientes(List<Cliente> list) {
        clientes.clear();
        clientes.addAll(list);
    }

    public void setReservas(List<Reserva> list) {
        reservas.clear();
        reservas.addAll(list);
    }


    // --- CARGAR ARCHIVO AL INICIAR ---
    private void cargarReservasDesdeArchivo() {
        List<String> lineas = Guardado.cargarArchivo();

        for (String l : lineas) {
            String[] p = l.split("\\|");

            if (p.length < 5) continue;

            String codigo = p[0];
            String documento = p[1];
            String titulo = p[2];
            int salaNum = Integer.parseInt(p[3]);
            String asientosTxt = p[4];

            Cliente c = new Cliente("NN", documento, "");
            registrarCliente(c);

            Pelicula peli = peliculas.stream()
                    .filter(x -> x.getTitulo().equalsIgnoreCase(titulo))
                    .findFirst().orElse(new Pelicula(titulo, "Desconocido", 0, "0"));

            Sala sala = new Sala(salaNum, 10, 10);

            Funcion f = new Funcion(peli, sala, java.time.LocalDate.now(), java.time.LocalTime.of(0, 0));

            List<Asiento> lista = new ArrayList<>();
            if (!asientosTxt.isEmpty()) {
                String[] arr = asientosTxt.split(";");
                for (String a : arr) {
                    if (a.trim().isEmpty()) continue;
                    String[] xy = a.split("-");
                    lista.add(new Asiento(xy[0].charAt(0), Integer.parseInt(xy[1])));
                }
            }

            Reserva r = new Reserva(codigo, c, f, lista);
            reservas.add(r);
        }
    }
    // -- dentro de Cine.java (añade después de otros métodos) --

/**
 * Reinicia (libera) todas las reservas y asientos asociados a una función.
 * También elimina las reservas del registro global de reservas y de los clientes.
 */
public void reiniciarFuncion(Funcion funcion) {
    if (funcion == null) return;

    // copiar lista para evitar ConcurrentModification
    List<Reserva> reservasFunc = new ArrayList<>(funcion.getReservas());

    for (Reserva r : reservasFunc) {
        // liberar asientos de la reserva
        for (Asiento a : r.getAsientos()) {
            a.liberar();
        }
        // eliminar reserva del cliente
        Cliente cli = r.getCliente();
        if (cli != null) {
            cli.getReservas().removeIf(x -> x.getCodigo().equals(r.getCodigo()));
        }
        // eliminar de lista global reservas
        this.reservas.removeIf(x -> x.getCodigo().equals(r.getCodigo()));
        // eliminar de la función
        funcion.getReservas().removeIf(x -> x.getCodigo().equals(r.getCodigo()));
    }
}

/**
 * Reinicia todo el sistema: libera asientos y borra reservas.
 * NO borra películas, salas o funciones (si quieres también borrarlas, lo adaptamos).
 */
public void reiniciarTodoReservas() {
    // liberar por cada función
    for (Funcion f : new ArrayList<>(funciones)) {
        reiniciarFuncion(f);
    }
    // limpiar lista global de reservas y del registro de clientes
    reservas.clear();
    for (Cliente c : clientes) c.getReservas().clear();
}

}


