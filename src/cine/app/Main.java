package cine.app;

import cine.sistema.Cine;
import cine.modelos.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Main de apoyo: mantiene el objeto Cine público y crea datos de ejemplo.
 * También contiene la versión consola (si la quieres usar).
 */
public class Main {

    // acceso público para la UI
    public static final Cine cine = new Cine();

    // Método que inicializa datos de prueba (lo llama MainGUI)
    public static void inicializarDatosPublico() {
        if (!cine.getFunciones().isEmpty()) return; // ya inicializado

        Pelicula p1 = new Pelicula3D("Avatar: Camino del Agua", "Aventura", 192, "+13");
        Pelicula p2 = new PeliculaAnimada("Coco", "Familiar", 105, "ATP");
        Pelicula p3 = new Pelicula2D("Parásitos", "Drama", 132, "+18");

        Sala s1 = new Sala(1, 6, 8);
        Sala s2 = new Sala3D(2, 5, 10);
        Sala s3 = new SalaVIP(3, 4, 6);

        Funcion f1 = new Funcion(p1, s1, LocalDate.now(), LocalTime.of(18,0));
        Funcion f2 = new Funcion(p2, s2, LocalDate.now(), LocalTime.of(20,30));
        Funcion f3 = new Funcion(p3, s3, LocalDate.now().plusDays(1), LocalTime.of(21,0));

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

    // Opción: versión consola rápida (no necesaria si usarás GUI)
    public static void main(String[] args) {
        inicializarDatosPublico();
        System.out.println("Ejecuta MainGUI para abrir la interfaz gráfica.");
    }
}


