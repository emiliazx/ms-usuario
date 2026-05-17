package com.costuras.usuario.excepciones;

public class AccesoDenegadoException extends RuntimeException {
  public AccesoDenegadoException() {

        super("No tienes permiso para modificar esta dirección");
    }
}
