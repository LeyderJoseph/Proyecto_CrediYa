package view.imprimir;

import view.Validaciones;

public class MenuCliente {

    private final Validaciones validaciones = new Validaciones();

    public int menu() {
        return validaciones.pedirEnteroEnRango("Menu Cliente\n1. Consultar mi informacion\n2. Consultar mis prestamos\n3. Filtrar mis prestamos por estado\n4. Consultar historico de pagos\n5. Cerrar sesion", 1, 5);
    }
}
