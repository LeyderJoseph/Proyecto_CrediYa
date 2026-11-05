package model;

import java.time.LocalDateTime;

public class Cliente extends Persona {

    private String telefono;

    public Cliente(int idPersona, String nombre, String documento, String correo, String clave, boolean activo, LocalDateTime fechaRegistro, String telefono) {
        super(idPersona, nombre, documento, correo, clave, activo, fechaRegistro);
        this.telefono = telefono;
    }

    @Override
    public String getTipoPerfil() {
        return "CLIENTE";
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
