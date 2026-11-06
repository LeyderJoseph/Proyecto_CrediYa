package view.ingreso;

import java.time.LocalDateTime;
import model.Cliente;
import view.Validaciones;

public class IngresoCliente {

    private final Validaciones validaciones = new Validaciones();

    public Cliente pedirCliente() {
        String nombre = validaciones.pedirTexto("Ingrese el nombre completo del cliente:");
        String documento = validaciones.pedirDocumento("Ingrese el documento del cliente:");
        String correo = validaciones.pedirCorreo("Ingrese el correo del cliente:");
        String clave = validaciones.pedirClave("Ingrese la clave del cliente:");
        String telefono = validaciones.pedirTelefono("Ingrese el telefono del cliente:");
        return new Cliente(0, nombre, documento, correo, clave, true, LocalDateTime.now(), telefono);
    }

    public int pedirIdCliente() {
        return validaciones.pedirEnteroEnRango("Ingrese el ID del cliente:", 1, Integer.MAX_VALUE);
    }

    public int pedirCampoActualizacion() {
        return validaciones.pedirEnteroEnRango("Actualizar cliente: 1. Nombre  2. Correo  3. Clave  4. Telefono  5. Cancelar", 1, 5);
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

    public String pedirTelefono() {
        return validaciones.pedirTelefono("Ingrese el nuevo telefono:");
    }
}
