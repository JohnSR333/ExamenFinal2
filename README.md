#  Sistema de Reservas de Cine  

## 📌 1. Análisis del Sistema  

###  Problema  
En muchos cines, el proceso de venta de boletos y asignación de asientos se hace manualmente, lo que ocasiona:  
- Doble asignación de asientos.  
- Pérdida de tiempo en la atención al cliente.  
- Dificultad para controlar funciones y disponibilidad de salas.  
- Imposibilidad de generar reportes de ventas o popularidad de películas.  

###  Propuesta 
Desarrollar un sistema en el que permita:  
- Registrar películas, salas y funciones.  
- Consultar disponibilidad de asientos en cada función.  
- Realizar reservas de manera segura.  
- Generar comprobantes de reserva para los clientes.  
- Llevar un control básico de la gestión del cine.  

###  Requisitos 
- Crear y gestionar películas.  
- Crear funciones (película, sala, fecha, hora).  
- Mostrar los asientos disponibles.  
- Permitir a los clientes reservar asiento(s).  
- Emitir un comprobante o confirmación de la reserva.

## 📌 2. Diseño del Sistema  

###  Clases Principales  
- **Pelicula** → título, género, duración.  
- **Sala** → número de sala, capacidad, lista de asientos.  
- **Asiento** → fila, número, estado (libre/ocupado).  
- **Funcion** → película, sala, fecha, hora, lista de asientos disponibles.  
- **Cliente** → nombre, documento, correo.  
- **Reserva** → cliente, función, asiento(s).  
- **Cine** → gestiona salas, funciones, películas y reservas.
  
  ## 📌 3. Diseño de Clases para el Sistema de Reservas de Cine

 ### 1. Clase Película

 **Atributos:**
título (String)
género (String)
duración (int, en minutos)
clasificación (String)
 - título (String)
 - género (String)
 - duración (int, en minutos)
 - clasificación (String)

 **Métodos:**
mostrarInfo() → imprime título, género, duración y clasificación.
esApta(edadCliente) → verifica si el cliente puede ver la película según clasificación.
 - mostrarInfo() → imprime título, género, duración y clasificación.
 - esApta(edadCliente) → verifica si el cliente puede ver la película según clasificación.

 **Posibles subclases:**
Pelicula2D
Pelicula3D
PeliculaAnimada (ejemplo si quieres diferenciar formatos o tipos).
 - Pelicula2D
 - Pelicula3D
 - PeliculaAnimada (ejemplo si quieres diferenciar formatos o tipos).

### 2. Clase Sala

 **Atributos:**
numero (int)
capacidad (int)
listaAsientos (ArrayList<Asiento>)
 - numero (int)
 - capacidad (int)
 - listaAsientos (ArrayList<Asiento>)

 **Métodos:**
mostrarDisponibilidad() → muestra los asientos libres.
ocuparAsiento(asiento) → cambia el estado de un asiento a ocupado.
liberarAsiento(asiento) → cambia el estado a libre.
 - mostrarDisponibilidad() → muestra los asientos libres.
 - ocuparAsiento(asiento) → cambia el estado de un asiento a ocupado.
 - liberarAsiento(asiento) → cambia el estado a libre.

 **Subclases (si se se desea alguna especificacion):**
Sala2D
Sala3D
SalaVIP
 - Sala2D
 - Sala3D
 - SalaVIP

### 3. Clase Asiento

 **Atributos:**
fila (char o int)
numero (int)
estado (booleano → ocupado/libre)
Métodos:
ocupar() → marca el asiento como ocupado.
liberar() → marca el asiento como libre.
estaDisponible() → devuelve true/false.
 - fila (char o int)
 - numero (int)
 - estado (booleano → ocupado/libre)
 **Métodos:**
 - ocupar() → marca el asiento como ocupado.
 - liberar() → marca el asiento como libre.
 - estaDisponible() → devuelve true/false.

### 4. Clase Función

 **Atributos:**
pelicula (Película)
sala (Sala)
fecha (Date)
hora (String u objeto LocalTime)
Métodos:
mostrarDetalles() → muestra película, sala, fecha y hora.
verDisponibilidad() → consulta disponibilidad de asientos en la sala.
 - pelicula (Película)
 - sala (Sala)
 - fecha (Date)
 - hora (String u objeto LocalTime)
 **Métodos:**
 - mostrarDetalles() → muestra película, sala, fecha y hora.
 - verDisponibilidad() → consulta disponibilidad de asientos en la sala.

### 5. Clase Cliente

 **Atributos:**
nombre (String)
documento (String)
correo (String)
 - nombre (String)
 - documento (String)
 - correo (String)

 **Métodos:**
reservarAsiento(funcion, asiento) → crea una reserva.
consultarReservas() → lista reservas hechas por el cliente.
 - reservarAsiento(funcion, asiento) → crea una reserva.
 - consultarReservas() → lista reservas hechas por el cliente.

 **Subclases:**
ClienteRegular
ClienteVIP (si quieres dar beneficios, descuentos, prioridad).
 - ClienteRegular
 - ClienteVIP (si quieres dar beneficios, descuentos, prioridad).

### 6. Clase Reserva

 **Atributos:**
cliente (Cliente)
funcion (Función)
asiento(s) (ArrayList<Asiento>)
 - cliente (Cliente)
 - funcion (Función)
 - asiento(s) (ArrayList<Asiento>)

 **Métodos:**
confirmar() → guarda la reserva.
cancelar() → libera los asientos.
mostrarTicket() → imprime comprobante de la reserva.
 - confirmar() → guarda la reserva.
 - cancelar() → libera los asientos.
 - mostrarTicket() → imprime comprobante de la reserva.

### 7. Clase Cine

 **Atributos:**
listaSalas (ArrayList<Sala>)
listaPeliculas (ArrayList<Pelicula>)
listaFunciones (ArrayList<Función>)
 - listaSalas (ArrayList<Sala>)
 - listaPeliculas (ArrayList<Pelicula>)
 - listaFunciones (ArrayList<Función>)

 **Métodos:**
agregarPelicula(pelicula)
agregarSala(sala)
programarFuncion(funcion)
mostrarCartelera() → lista las funciones disponibles.
 - agregarPelicula(pelicula)
 - agregarSala(sala)
 - programarFuncion(funcion)
 - mostrarCartelera() → lista las funciones disponibles.

### 🔹 Ejemplo de Herencia (subclases útiles)

Cliente → ClienteRegular, ClienteVIP.
 - Cliente → ClienteRegular, ClienteVIP.

Sala → Sala2D, Sala3D, SalaVIP.
 - Sala → Sala2D, Sala3D, SalaVIP.

Pelicula → Pelicula2D, Pelicula3D, PeliculaAnimada.  
 - Pelicula → Pelicula2D, Pelicula3D, PeliculaAnimada.  