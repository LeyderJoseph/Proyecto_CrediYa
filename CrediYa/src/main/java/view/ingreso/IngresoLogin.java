package view.ingreso;

import view.Validaciones;

public class IngresoLogin {

    private final Validaciones validaciones = new Validaciones();

    public String pedirDocumento() {
        return validaciones.pedirDocumento("Ingrese su documento:");
    }

    public String pedirClave() {
        return validaciones.pedirClave("Ingrese su clave:");
    }
}
