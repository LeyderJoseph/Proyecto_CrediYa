package view.imprimir;

import java.util.List;
import model.Empleado;

public class ImprimirEmpleado {

    public void entidad(Empleado empleado) {
        System.out.println("ID: " + empleado.getIdPersona() + " | Nombre: " + empleado.getNombre() + " | Rol: " + empleado.getRol() + " | Salario: " + empleado.getSalario() + " | Activo: " + empleado.isActivo());
    }

    public void lista(List<Empleado> empleados) {
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados.");
            return;
        }
        empleados.forEach(this::entidad);
    }
}
