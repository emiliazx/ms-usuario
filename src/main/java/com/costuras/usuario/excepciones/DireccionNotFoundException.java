package com.costuras.usuario.excepciones;


public class DireccionNotFoundException extends RuntimeException {
    public DireccionNotFoundException(Integer id) {
        super("Dirección con id " + id + " no encontrada");
    }
}
