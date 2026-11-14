package view.imprimir;

import view.Validaciones;

public class MenuReporte {

    private final Validaciones validaciones = new Validaciones();

    public int menu() {
        return validaciones.pedirEnteroEnRango("Reportes\n1. Prestamos activos\n2. Prestamos vencidos\n3. Clientes morosos\n4. Total prestado\n5. Total recaudado\n6. Saldo total de cartera\n7. Resumen de prestamos\n8. Regresar", 1, 8);
    }
}
