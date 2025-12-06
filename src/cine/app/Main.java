package cine.app;

import cine.excepciones.AsientoOcupadoException;
import cine.modelos.*;
import cine.sistema.Cine;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final Cine cine = new Cine();

    public static void main(String[] args) {
        inicializarDatos();
        int opcion;
        do {
            mostrarMenu();
            opcion = leerInt("Seleccione opción: ");
            switch (opcion) {
                case 1 -> cine.mostrarCartelera();
                case 2 -> verDisponibilidad();
                case 3 -> reservarAsientos();
                case 4 -> mostrarReservasCliente();
                case 5 -> System.out.println("Saliendo... ¡BUEN DIA!");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 5);
    }

    private static void mostrarMenu() {
        System.out.println("\n=== SISTEMA DE RESERVAS ===");
        System.out.println("1. Mostrar cartelera");
        System.out.println("2. Ver disponibilidad de una función");
        System.out.println("3. Reservar asiento(s)");
        System.out.println("4. Mostrar reservas de un cliente (por documento)");
        System.out.println("5. Salir");
    }

    private static void inicializarDatos() {
        Pelicula p1 = new Pelicula3D("Avatar: Camino del Agua", "Aventura", 192, "+13");
        Pelicula p2 = new PeliculaAnimada("Coco", "Familiar", 105, "ATP");
        Pelicula p3 = new Pelicula2D("Parásitos", "Drama", 132, "+18");

        Sala s1 = new Sala(1, 6, 8); 
        Sala s2 = new Sala3D(2, 5, 10);
        Sala s3 = new SalaVIP(3, 4, 6);

        Funcion f1 = new Funcion(p1, s1, LocalDate.now(), LocalTime.of(18, 0));
        Funcion f2 = new Funcion(p2, s2, LocalDate.now(), LocalTime.of(20, 30));
        Funcion f3 = new Funcion(p3, s3, LocalDate.now().plusDays(1), LocalTime.of(21, 0));

        cine.agregarPelicula(p1);
        cine.agregarPelicula(p2);
        cine.agregarPelicula(p3);

        cine.agregarSala(s1);
        cine.agregarSala(s2);
        cine.agregarSala(s3);

        cine.programarFuncion(f1);
        cine.programarFuncion(f2);
        cine.programarFuncion(f3);
    }

    private static void verDisponibilidad() {
        cine.mostrarCartelera();
        int idx = leerInt("Ingrese índice de la función para ver disponibilidad: ");
        Optional<Funcion> opt = cine.buscarFuncionPorIndice(idx);
        if (opt.isEmpty()) { System.out.println("Función no encontrada."); return; }
        Funcion f = opt.get();
        f.getSala().mostrarDisponibilidad();
    }

private static void reservarAsientos() {
    cine.mostrarCartelera();
    int idx = leerInt("Ingrese índice de la función a reservar: ");
    Optional<Funcion> opt = cine.buscarFuncionPorIndice(idx);
    if (opt.isEmpty()) { System.out.println("Función inválida."); return; }
    Funcion f = opt.get();

    System.out.println("Ingrese datos del cliente:");
    String nombre = leerString("Nombre: ");
    String documento = leerString("Documento: ");
    String correo = leerString("Correo: ");

    Cliente cliente;
    String tipo = leerString("¿VIP? (s/n): ");
    if (tipo.equalsIgnoreCase("s")) cliente = new ClienteVIP(nombre, documento, correo, 1);
    else cliente = new ClienteRegular(nombre, documento, correo);

    cine.registrarCliente(cliente);

    int cantidad = leerInt("¿Cuántos asientos desea reservar?: ");
    List<Asiento> seleccion = new ArrayList<>();

    for (int i = 0; i < cantidad; i++) {
        System.out.println("Asiento " + (i+1) + ":");
        char fila = leerString("Fila (A-Z): ").toUpperCase().charAt(0);
        int num = leerInt("Número: ");
        Asiento a = f.getSala().buscarAsiento(fila, num);
        if (a == null) {
            System.out.println("Asiento no existe. Intente otro.");
            i--; continue;
        }
        if (!a.estaDisponible()) {
            System.out.println("Asiento ocupado. Elija otro.");
            i--; continue;
        }
        seleccion.add(a);
    }

    System.out.println("Resumen reserva para " + cliente.getNombre());
    System.out.println("Película: " + f.getPelicula().getTitulo());
    System.out.print("Asientos: ");
    seleccion.forEach(a -> System.out.print(a.getFila() + "" + a.getNumero() + " "));
    System.out.println();

    String confirmar = leerString("Confirmar reserva? (s/n): ");
    if (confirmar.equalsIgnoreCase("s")) {
        try {
            Reserva r = cliente.hacerReserva(f, seleccion);

            cine.registrarReserva(r);

            r.mostrarTicket();
        } catch (Exception e) {
            System.out.println("Error al realizar la reserva: " + e.getMessage());
        }
    } else System.out.println("Reserva cancelada por usuario.");
}

private static void mostrarReservasCliente() {
    String documento = leerString("Ingrese documento del cliente: ");

    List<Reserva> lista = cine.reservasPorDocumento(documento);

    if (lista.isEmpty()) {
        System.out.println("El cliente no tiene reservas registradas.");
        return;
    }

    System.out.println("===== RESERVAS DEL CLIENTE =====");
    for (Reserva r : lista) {
        r.mostrarTicket();
        System.out.println("-------------------------------");
    }
}


    private static int leerInt(String mensaje) {
        System.out.print(mensaje);
        while (!sc.hasNextInt()) {
            sc.next(); 
            System.out.print("Entrada inválida. " + mensaje);
        }
        int v = sc.nextInt();
        sc.nextLine();
        return v;
    }

    private static String leerString(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }
}

