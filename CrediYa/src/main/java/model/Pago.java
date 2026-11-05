package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pago implements Serializable {

    private int idPago;
    private Prestamo prestamo;
    private Empleado empleadoRegistrador;
    private LocalDateTime fechaPago;
    private double valorTotal;
    private String observacion;
    private List<DetallePago> detalles;

    public Pago(int idPago, Prestamo prestamo, Empleado empleadoRegistrador, LocalDateTime fechaPago, double valorTotal, String observacion) {
        this.idPago = idPago;
        this.prestamo = prestamo;
        this.empleadoRegistrador = empleadoRegistrador;
        this.fechaPago = fechaPago;
        this.valorTotal = valorTotal;
        this.observacion = observacion;
        this.detalles = new ArrayList<>();
    }

    public int getIdPago() { return idPago; }
    public void setIdPago(int idPago) { this.idPago = idPago; }
    public Prestamo getPrestamo() { return prestamo; }
    public void setPrestamo(Prestamo prestamo) { this.prestamo = prestamo; }
    public Empleado getEmpleadoRegistrador() { return empleadoRegistrador; }
    public void setEmpleadoRegistrador(Empleado empleadoRegistrador) { this.empleadoRegistrador = empleadoRegistrador; }
    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }
    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public List<DetallePago> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePago> detalles) { this.detalles = detalles; }
}
