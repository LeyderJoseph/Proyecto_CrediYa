package view.imprimir;

import view.Validaciones;

public class MenuRespaldo {

    private final Validaciones validaciones = new Validaciones();

    public int menu() {
        return validaciones.pedirEnteroEnRango("Respaldos\n1. Exportar empleados\n2. Exportar clientes\n3. Exportar prestamos\n4. Exportar pagos\n5. Regresar", 1, 5);
    }
}
