package view.imprimir;

public class Mensajes {

    public void exito(String mensaje) {
        System.out.println("Proceso exitoso: " + mensaje);
    }

    public void informativo(String mensaje) {
        System.out.println(mensaje);
    }

    public void error(String mensaje) {
        System.out.println("Error: " + mensaje);
    }
}
