package view.ingreso;

import model.EstadoPrestamo;
import view.Validaciones;

public class IngresoPrestamo {

    private final Validaciones validaciones = new Validaciones();

    public int pedirIdCliente() {
        return validaciones.pedirEnteroEnRango("Ingrese el ID del cliente:", 1, Integer.MAX_VALUE);
    }

    public int pedirIdPrestamo() {
        return validaciones.pedirEnteroEnRango("Ingrese el ID del prestamo:", 1, Integer.MAX_VALUE);
    }

    public double pedirCapital() {
        return validaciones.pedirDecimalMayorQueCero("Ingrese el capital solicitado:");
    }

    public int pedirPlazo() {
        return validaciones.pedirEnteroEnRango("Ingrese el plazo en meses:", 1, 60);
    }

    public boolean pedirDecision() {
        int opcion = validaciones.pedirEnteroEnRango("Seleccione decision: 1. Aprobar  2. Rechazar", 1, 2);
        return opcion == 1;
    }

    public String pedirObservacion() {
        return validaciones.pedirCadenaNoVacia("Ingrese la observacion:");
    }

    public EstadoPrestamo pedirEstado() {
        int opcion = validaciones.pedirEnteroEnRango("Filtre por estado: 1. Pendiente  2. Activo  3. Pagado  4. Rechazado  5. Vencido", 1, 5);
        return EstadoPrestamo.values()[opcion - 1];
    }

    public int pedirIdEmpleadoRadicador() {
        return validaciones.pedirEnteroEnRango("Ingrese el ID del asesor o empleado radicador:", 1, Integer.MAX_VALUE);
    }
}
