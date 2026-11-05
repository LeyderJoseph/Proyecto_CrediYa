package model;

import java.io.Serializable;

public class DetallePago implements Serializable {

    private int idDetallePago;
    private MetodoPago metodoPago;
    private TipoTransferencia tipoTransferencia;
    private double valor;
    private String referencia;

    public DetallePago(int idDetallePago, MetodoPago metodoPago, TipoTransferencia tipoTransferencia, double valor, String referencia) {
        this.idDetallePago = idDetallePago;
        this.metodoPago = metodoPago;
        this.tipoTransferencia = tipoTransferencia;
        this.valor = valor;
        this.referencia = referencia;
    }

    public int getIdDetallePago() { return idDetallePago; }
    public void setIdDetallePago(int idDetallePago) { this.idDetallePago = idDetallePago; }
    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }
    public TipoTransferencia getTipoTransferencia() { return tipoTransferencia; }
    public void setTipoTransferencia(TipoTransferencia tipoTransferencia) { this.tipoTransferencia = tipoTransferencia; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
}
