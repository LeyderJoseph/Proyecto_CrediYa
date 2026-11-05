package model;

import java.io.Serializable;

public class PoliticaCredito implements Serializable {

    private int idPolitica;
    private final String tipoInteres = "COMPUESTO";
    private double tasaInteresMensual;
    private double montoMinimo;
    private double montoMaximo;
    private int plazoMinimoMeses;
    private int plazoMaximoMeses;
    private double topeAsesor;
    private double topeSupervisor;
    private boolean activo;

    public PoliticaCredito(int idPolitica, double tasaInteresMensual, double montoMinimo, double montoMaximo, int plazoMinimoMeses, int plazoMaximoMeses, double topeAsesor, double topeSupervisor, boolean activo) {
        this.idPolitica = idPolitica;
        this.tasaInteresMensual = tasaInteresMensual;
        this.montoMinimo = montoMinimo;
        this.montoMaximo = montoMaximo;
        this.plazoMinimoMeses = plazoMinimoMeses;
        this.plazoMaximoMeses = plazoMaximoMeses;
        this.topeAsesor = topeAsesor;
        this.topeSupervisor = topeSupervisor;
        this.activo = activo;
    }

    public int getIdPolitica() {
        return idPolitica;
    }

    public void setIdPolitica(int idPolitica) {
        this.idPolitica = idPolitica;
    }

    public String getTipoInteres() {
        return tipoInteres;
    }

    public double getTasaInteresMensual() {
        return tasaInteresMensual;
    }

    public void setTasaInteresMensual(double tasaInteresMensual) {
        this.tasaInteresMensual = tasaInteresMensual;
    }

    public double getMontoMinimo() {
        return montoMinimo;
    }

    public void setMontoMinimo(double montoMinimo) {
        this.montoMinimo = montoMinimo;
    }

    public double getMontoMaximo() {
        return montoMaximo;
    }

    public void setMontoMaximo(double montoMaximo) {
        this.montoMaximo = montoMaximo;
    }

    public int getPlazoMinimoMeses() {
        return plazoMinimoMeses;
    }

    public void setPlazoMinimoMeses(int plazoMinimoMeses) {
        this.plazoMinimoMeses = plazoMinimoMeses;
    }

    public int getPlazoMaximoMeses() {
        return plazoMaximoMeses;
    }

    public void setPlazoMaximoMeses(int plazoMaximoMeses) {
        this.plazoMaximoMeses = plazoMaximoMeses;
    }

    public double getTopeAsesor() {
        return topeAsesor;
    }

    public void setTopeAsesor(double topeAsesor) {
        this.topeAsesor = topeAsesor;
    }

    public double getTopeSupervisor() {
        return topeSupervisor;
    }

    public void setTopeSupervisor(double topeSupervisor) {
        this.topeSupervisor = topeSupervisor;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
