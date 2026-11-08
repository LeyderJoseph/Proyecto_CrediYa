package view.imprimir;

import java.util.List;
import model.HistorialAprobacion;

public class ImprimirHistorialAprobacion {

    public void lista(List<HistorialAprobacion> historial) {
        if (historial.isEmpty()) {
            System.out.println("No hay historial de aprobacion para este prestamo.");
            return;
        }
        historial.forEach(item -> System.out.println(
                "Fecha: " + item.getFechaAccion()
                + " | Empleado: " + item.getEmpleado().getNombre()
                + " | Rol: " + item.getRolEmpleado()
                + " | Accion: " + item.getAccion()
                + " | Observacion: " + item.getObservacion()
        ));
    }
}
