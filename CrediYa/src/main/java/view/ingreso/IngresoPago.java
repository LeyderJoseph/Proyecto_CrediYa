package view.ingreso;

import model.DetallePago;
import model.MetodoPago;
import model.TipoTransferencia;
import view.Validaciones;

public class IngresoPago {

    private final Validaciones validaciones = new Validaciones();

    public int pedirIdPrestamo() {
        return validaciones.pedirEnteroEnRango("Ingrese el ID del prestamo:", 1, Integer.MAX_VALUE);
    }

    public double pedirValorTotal(double saldo) {
        return validaciones.pedirDecimalEnRango("Ingrese el valor total del abono:", 0.01, saldo);
    }

    public DetallePago pedirDetalle(double restante) {
        int metodo = validaciones.pedirEnteroEnRango("Seleccione medio de pago: 1. Efectivo  2. Transferencia", 1, 2);
        double valor = validaciones.pedirDecimalEnRango("Ingrese el valor para este medio. Restante: " + restante, 0.01, restante);
        if (metodo == 1) {
            return new DetallePago(0, MetodoPago.EFECTIVO, null, valor, null);
        }
        int tipo = validaciones.pedirEnteroEnRango("Seleccione transferencia: 1. PSE  2. Nequi  3. Bancolombia  4. Davivienda", 1, 4);
        String referencia = validaciones.pedirCadenaNoVacia("Ingrese la referencia de la transferencia:");
        return new DetallePago(0, MetodoPago.TRANSFERENCIA, TipoTransferencia.values()[tipo - 1], valor, referencia);
    }

    public String pedirObservacion() {
        return validaciones.pedirCadenaNoVacia("Ingrese la observacion del pago:");
    }
}
