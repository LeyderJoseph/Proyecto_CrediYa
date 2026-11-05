package model;

import java.time.LocalDateTime;

public class Empleado extends Persona {

    private RolEmpleado rol;
    private double salario;

    public Empleado(int idPersona, String nombre, String documento, String correo, String clave, boolean activo, LocalDateTime fechaRegistro, RolEmpleado rol, double salario) {
        super(idPersona, nombre, documento, correo, clave, activo, fechaRegistro);
        this.rol = rol;
        this.salario = salario;
    }

    @Override
    public String getTipoPerfil() {
        return "EMPLEADO";
    }

    public RolEmpleado getRol() {
        return rol;
    }

    public void setRol(RolEmpleado rol) {
        this.rol = rol;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }
}
