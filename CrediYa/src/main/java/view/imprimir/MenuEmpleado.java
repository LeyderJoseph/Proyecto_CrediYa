package view.imprimir;

import model.RolEmpleado;
import view.Validaciones;

public class MenuEmpleado {

    private final Validaciones validaciones = new Validaciones();

    public int menu(RolEmpleado rol) {
        if (rol == RolEmpleado.GERENTE) {
            return validaciones.pedirEnteroEnRango("Menu Gerente\n1. Registrar empleado\n2. Listar empleados\n3. Actualizar empleado\n4. Desactivar empleado\n5. Registrar cliente\n6. Listar clientes\n7. Actualizar cliente\n8. Desactivar cliente\n9. Radicar prestamo\n10. Gestionar prestamos pendientes\n11. Listar prestamos\n12. Registrar pago\n13. Consultar historico de pagos\n14. Ver reportes\n15. Exportar respaldos\n16. Cerrar sesion", 1, 16);
        }
        return validaciones.pedirEnteroEnRango("Menu " + rol + "\n1. Registrar cliente\n2. Listar clientes\n3. Actualizar cliente\n4. Radicar prestamo\n5. Gestionar prestamos pendientes\n6. Listar prestamos\n7. Registrar pago\n8. Consultar historico de pagos\n9. Cerrar sesion", 1, 9);
    }
}
