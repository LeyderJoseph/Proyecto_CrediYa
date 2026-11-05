package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class Persona implements Serializable {

    private int idPersona;
    private String nombre;
    private String documento;
    private String correo;
    private String clave;
    private boolean activo;
    private LocalDateTime fechaRegistro;

    protected Persona(int idPersona, String nombre, String documento, String correo, String clave, boolean activo, LocalDateTime fechaRegistro) {
        this.idPersona = idPersona;
        this.nombre = nombre;
        this.documento = documento;
        this.correo = correo;
        this.clave = clave;
        this.activo = activo;
        this.fechaRegistro = fechaRegistro;
    }

    public abstract String getTipoPerfil();

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
