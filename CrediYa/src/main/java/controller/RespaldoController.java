package controller;

import dao.ClienteDAO;
import dao.EmpleadoDAO;
import dao.PagoDAO;
import dao.PrestamoDAO;
import service.ArchivoBackup;
import view.imprimir.MenuRespaldo;
import view.imprimir.Mensajes;

public class RespaldoController {

    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final PrestamoDAO prestamoDAO = new PrestamoDAO();
    private final PagoDAO pagoDAO = new PagoDAO();
    private final ArchivoBackup archivo = new ArchivoBackup();
    private final MenuRespaldo menu = new MenuRespaldo();
    private final Mensajes mensajes = new Mensajes();

    public void menu() {
        int opcion;
        do {
            opcion = menu.menu();
            boolean exportado = switch (opcion) {
                case 1 -> archivo.exportar(empleadoDAO.listar(), "empleados.dat");
                case 2 -> archivo.exportar(clienteDAO.listar(), "clientes.dat");
                case 3 -> archivo.exportar(prestamoDAO.listar(), "prestamos.dat");
                case 4 -> archivo.exportar(pagoDAO.listarTodos(), "pagos.dat");
                case 5 -> false;
                default -> false;
            };
            if (exportado) {
                mensajes.exito("Respaldo exportado correctamente.");
            }
        } while (opcion != 5);
    }
}
