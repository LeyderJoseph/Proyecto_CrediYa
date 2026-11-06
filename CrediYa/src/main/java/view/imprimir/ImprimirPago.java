package view.imprimir;

import java.util.List;
import model.DetallePago;
import model.Pago;

public class ImprimirPago {

    public void entidad(Pago pago) {
        System.out.println("Pago ID: " + pago.getIdPago() + " | Fecha: " + pago.getFechaPago() + " | Total: " + pago.getValorTotal() + " | Registro: " + pago.getEmpleadoRegistrador().getNombre());
        for (DetallePago detalle : pago.getDetalles()) {
            String medio = detalle.getMetodoPago().toString();
            if (detalle.getTipoTransferencia() != null) {
                medio += " - " + detalle.getTipoTransferencia() + " - Ref: " + detalle.getReferencia();
            }
            System.out.println("  " + medio + " | Valor: " + detalle.getValor());
        }
    }

    public void lista(List<Pago> pagos) {
        if (pagos.isEmpty()) {
            System.out.println("No hay pagos registrados para este prestamo.");
            return;
        }
        pagos.forEach(this::entidad);
    }
}
