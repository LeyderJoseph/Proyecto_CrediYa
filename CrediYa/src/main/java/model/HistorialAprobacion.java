package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class HistorialAprobacion implements Serializable {

    private int idHistorial;
    private Prestamo prestamo;
    private Empleado empleado;
    private RolEmpleado rolEmpleado;
    private AccionAprobacion accion;
    private LocalDateTime fechaAccion;
    private String observacion;

    public HistorialAprobacion(int idHistorial, Prestamo prestamo, Empleado empleado, RolEmpleado rolEmpleado, AccionAprobacion accion, LocalDateTime fechaAccion, String observacion) {
        this.idHistorial = idHistorial;
        this.prestamo = prestamo;
        this.empleado = empleado;
        this.rolEmpleado = rolEmpleado;
        this.accion = accion;
        this.fechaAccion = fechaAccion;
        this.observacion = observacion;
    }

    public int getIdHistorial() { return idHistorial; }
    public void setIdHistorial(int idHistorial) { this.idHistorial = idHistorial; }
    public Prestamo getPrestamo() { return prestamo; }
    public void setPrestamo(Prestamo prestamo) { this.prestamo = prestamo; }
    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }
    public RolEmpleado getRolEmpleado() { return rolEmpleado; }
    public void setRolEmpleado(RolEmpleado rolEmpleado) { this.rolEmpleado = rolEmpleado; }
    public AccionAprobacion getAccion() { return accion; }
    public void setAccion(AccionAprobacion accion) { this.accion = accion; }
    public LocalDateTime getFechaAccion() { return fechaAccion; }
    public void setFechaAccion(LocalDateTime fechaAccion) { this.fechaAccion = fechaAccion; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
