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

    public List<Funcion> funcionesPorPelicula(String titulo) {
        return funciones.stream()
                .filter(f -> f.getPelicula().getTitulo().equalsIgnoreCase(titulo))
                .collect(Collectors.toList());
    }

    public void mostrarCartelera() {
        if (funciones.isEmpty()) {
            System.out.println("No hay funciones programadas.");
            return;
        }
        System.out.println("===== CARTELERA =====");
        for (int i = 0; i < funciones.size(); i++) {
            System.out.printf("[%d] %s%n", i, funciones.get(i).toString());
        }
    }

    public void registrarCliente(Cliente c) {
        boolean existe = clientes.stream()
                .anyMatch(x -> x.getDocumento().equalsIgnoreCase(c.getDocumento()));

        if (!existe) {
            clientes.add(c);
        }
    }

    public void registrarReserva(Reserva r) {
        reservas.add(r);
    }

    public List<Reserva> reservasPorDocumento(String documento) {
        return reservas.stream()
                .filter(r -> r.getCliente().getDocumento().equalsIgnoreCase(documento))
                .collect(Collectors.toList());
    }

    public List<Cliente> getClientes() {
        return clientes;
    }
}

