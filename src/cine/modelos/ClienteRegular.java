package cine.modelos;

public class ClienteRegular extends Cliente {
    public ClienteRegular(String nombre, String documento, String correo) {
        super(nombre, documento, correo);
    }
    public ClienteRegular(String id, String nombre, String documento, String correo) {
        super(id, nombre, documento, correo);
    }
}


