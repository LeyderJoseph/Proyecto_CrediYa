package view.ingreso;

import java.time.LocalDateTime;
import model.Empleado;
import model.RolEmpleado;
import view.Validaciones;

public class IngresoEmpleado {

    private final Validaciones validaciones = new Validaciones();

    public Empleado pedirGerenteInicial() {
        String nombre = validaciones.pedirTexto("Ingrese el nombre completo del gerente:");
        String documento = validaciones.pedirDocumento("Ingrese el documento del gerente:");
        String correo = validaciones.pedirCorreo("Ingrese el correo del gerente:");
        String clave = validaciones.pedirClave("Ingrese la clave del gerente:");
        double salario = validaciones.pedirDecimalMayorQueCero("Ingrese el salario del gerente:");
        return new Empleado(0, nombre, documento, correo, clave, true, LocalDateTime.now(), RolEmpleado.GERENTE, salario);
    }

    public Empleado pedirEmpleado() {
        String nombre = validaciones.pedirTexto("Ingrese el nombre completo del empleado:");
        String documento = validaciones.pedirDocumento("Ingrese el documento del empleado:");
        String correo = validaciones.pedirCorreo("Ingrese el correo del empleado:");
        String clave = validaciones.pedirClave("Ingrese la clave del empleado:");
        double salario = validaciones.pedirDecimalMayorQueCero("Ingrese el salario del empleado:");
        int opcionRol = validaciones.pedirEnteroEnRango("Seleccione rol: 1. Asesor  2. Supervisor  3. Gerente", 1, 3);
        RolEmpleado rol = RolEmpleado.values()[opcionRol - 1];
        return new Empleado(0, nombre, documento, correo, clave, true, LocalDateTime.now(), rol, salario);
    }

    public int pedirIdEmpleado() {
        return validaciones.pedirEnteroEnRango("Ingrese el ID del empleado:", 1, Integer.MAX_VALUE);
    }

    public int pedirCampoActualizacion() {
        return validaciones.pedirEnteroEnRango("Actualizar empleado: 1. Nombre  2. Correo  3. Clave  4. Rol  5. Salario  6. Cancelar", 1, 6);
    }

    public String pedirNombre() {
        return validaciones.pedirTexto("Ingrese el nuevo nombre:");
    }

    public String pedirCorreo() {
        return validaciones.pedirCorreo("Ingrese el nuevo correo:");
    }

    public String pedirClave() {
        return validaciones.pedirClave("Ingrese la nueva clave:");
    }

    public RolEmpleado pedirRol() {
        int opcion = validaciones.pedirEnteroEnRango("Seleccione rol: 1. Asesor  2. Supervisor  3. Gerente", 1, 3);
        return RolEmpleado.values()[opcion - 1];
    }

    public double pedirSalario() {
        return validaciones.pedirDecimalMayorQueCero("Ingrese el nuevo salario:");
    }
}
