package view.imprimir;

import view.Validaciones;

public class MenuPrincipal {

    private final Validaciones validaciones = new Validaciones();

    public int menu() {
        return validaciones.pedirEnteroEnRango("CrediYa S.A.S.\n1. Iniciar sesion\n2. Salir", 1, 2);
    }

    public int seleccionarPerfil() {
        return validaciones.pedirEnteroEnRango("Seleccione el perfil de ingreso:\n1. Empleado\n2. Cliente", 1, 2);
    }
}
