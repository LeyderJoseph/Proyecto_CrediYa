package view.imprimir;

import model.RolEmpleado;
import view.Validaciones;

public class MenuEmpleado {

    private final Validaciones validaciones = new Validaciones();

    public int menu(RolEmpleado rol) {
        if (rol == RolEmpleado.GERENTE) {
            return validaciones.pedirEnteroEnRango("Menu Gerente\n1. Registrar empleado\n2. Listar empleados\n3. Actualizar empleado\n4. Desactivar empleado\n5. Registrar cliente\n6. Listar clientes\n7. Actualizar cliente\n8. Desactivar cliente\n9. Radicar prestamo\n10. Gestionar prestamos pendientes\n11. Listar prestamos\n12. Filtrar prestamos por radicador\n13. Consultar historial de aprobacion\n14. Registrar pago\n15. Consultar historico de pagos\n16. Ver reportes\n17. Exportar respaldos\n18. Cerrar sesion", 1, 18);
        }
        return validaciones.pedirEnteroEnRango("Menu " + rol + "\n1. Registrar cliente\n2. Listar clientes\n3. Actualizar cliente\n4. Radicar prestamo\n5. Gestionar prestamos pendientes\n6. Listar prestamos\n7. Filtrar prestamos por radicador\n8. Consultar historial de aprobacion\n9. Registrar pago\n10. Consultar historico de pagos\n11. Cerrar sesion", 1, 11);
    }
}
