package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Prestamo implements Serializable {

    private int idPrestamo;
    private Cliente cliente;
    private Empleado empleadoRadicador;
    private Empleado empleadoAprobador;
    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaAprobacion;
    private double capital;
    private final String tipoInteres = "COMPUESTO";
    private double tasaInteresMensual;
    private int plazoMeses;
    private double interesTotal;
    private double montoTotal;
    private double valorCuota;
    private double saldoPendiente;
    private LocalDate fechaVencimiento;
    private EstadoPrestamo estado;
    private String observacion;

    public Prestamo(int idPrestamo, Cliente cliente, Empleado empleadoRadicador, Empleado empleadoAprobador, LocalDateTime fechaSolicitud, LocalDateTime fechaAprobacion, double capital, double tasaInteresMensual, int plazoMeses, double interesTotal, double montoTotal, double valorCuota, double saldoPendiente, LocalDate fechaVencimiento, EstadoPrestamo estado, String observacion) {
        this.idPrestamo = idPrestamo;
        this.cliente = cliente;
        this.empleadoRadicador = empleadoRadicador;
        this.empleadoAprobador = empleadoAprobador;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaAprobacion = fechaAprobacion;
        this.capital = capital;
        this.tasaInteresMensual = tasaInteresMensual;
        this.plazoMeses = plazoMeses;
        this.interesTotal = interesTotal;
        this.montoTotal = montoTotal;
        this.valorCuota = valorCuota;
        this.saldoPendiente = saldoPendiente;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
        this.observacion = observacion;
    }

    public int getIdPrestamo() { return idPrestamo; }
    public void setIdPrestamo(int idPrestamo) { this.idPrestamo = idPrestamo; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Empleado getEmpleadoRadicador() { return empleadoRadicador; }
    public void setEmpleadoRadicador(Empleado empleadoRadicador) { this.empleadoRadicador = empleadoRadicador; }
    public Empleado getEmpleadoAprobador() { return empleadoAprobador; }
    public void setEmpleadoAprobador(Empleado empleadoAprobador) { this.empleadoAprobador = empleadoAprobador; }
    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDateTime fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }
    public LocalDateTime getFechaAprobacion() { return fechaAprobacion; }
    public void setFechaAprobacion(LocalDateTime fechaAprobacion) { this.fechaAprobacion = fechaAprobacion; }
    public double getCapital() { return capital; }
    public void setCapital(double capital) { this.capital = capital; }
    public String getTipoInteres() { return tipoInteres; }
    public double getTasaInteresMensual() { return tasaInteresMensual; }
    public void setTasaInteresMensual(double tasaInteresMensual) { this.tasaInteresMensual = tasaInteresMensual; }
    public int getPlazoMeses() { return plazoMeses; }
    public void setPlazoMeses(int plazoMeses) { this.plazoMeses = plazoMeses; }
    public double getInteresTotal() { return interesTotal; }
    public void setInteresTotal(double interesTotal) { this.interesTotal = interesTotal; }
    public double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(double montoTotal) { this.montoTotal = montoTotal; }
    public double getValorCuota() { return valorCuota; }
    public void setValorCuota(double valorCuota) { this.valorCuota = valorCuota; }
    public double getSaldoPendiente() { return saldoPendiente; }
    public void setSaldoPendiente(double saldoPendiente) { this.saldoPendiente = saldoPendiente; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    public EstadoPrestamo getEstado() { return estado; }
    public void setEstado(EstadoPrestamo estado) { this.estado = estado; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
