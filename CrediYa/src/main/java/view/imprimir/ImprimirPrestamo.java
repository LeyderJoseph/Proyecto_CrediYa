package view.imprimir;

import java.util.List;
import model.Prestamo;

public class ImprimirPrestamo {

    public void entidad(Prestamo prestamo) {
        System.out.println("ID: " + prestamo.getIdPrestamo()
                + " | Cliente: " + prestamo.getCliente().getNombre()
                + " | Capital: " + prestamo.getCapital()
                + " | Total: " + prestamo.getMontoTotal()
                + " | Cuota: " + prestamo.getValorCuota()
                + " | Saldo: " + prestamo.getSaldoPendiente()
                + " | Estado: " + prestamo.getEstado());
    }

    public void lista(List<Prestamo> prestamos) {
        if (prestamos.isEmpty()) {
            System.out.println("No hay prestamos para mostrar.");
            return;
        }
        prestamos.forEach(this::entidad);
    }
}
