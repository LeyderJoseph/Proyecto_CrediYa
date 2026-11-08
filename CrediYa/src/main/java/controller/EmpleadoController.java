package controller;

import dao.EmpleadoDAO;
import java.util.List;
import model.Empleado;
import model.RolEmpleado;
import view.ingreso.IngresoEmpleado;
import view.imprimir.ImprimirEmpleado;
import view.imprimir.MenuEmpleado;
import view.imprimir.Mensajes;

public class EmpleadoController {

    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private final IngresoEmpleado ingreso = new IngresoEmpleado();
    private final ImprimirEmpleado imprimir = new ImprimirEmpleado();
    private final MenuEmpleado menu = new MenuEmpleado();
    private final Mensajes mensajes = new Mensajes();
    private final ClienteController clienteController = new ClienteController();
    private final PrestamoController prestamoController = new PrestamoController();
    private final PagoController pagoController = new PagoController();
    private final ReporteController reporteController = new ReporteController();
    private final RespaldoController respaldoController = new RespaldoController();

    public void menu(Empleado empleado) {
        int opcion;
        do {
            opcion = menu.menu(empleado.getRol());
            if (empleado.getRol() == RolEmpleado.GERENTE) {
                ejecutarMenuGerente(opcion, empleado);
            } else {
                ejecutarMenuEmpleado(opcion, empleado);
            }
        } while (opcion != (empleado.getRol() == RolEmpleado.GERENTE ? 18 : 11));
    }

    private void ejecutarMenuGerente(int opcion, Empleado empleado) {
        switch (opcion) {
            case 1 -> registrar();
            case 2 -> listar();
            case 3 -> actualizar();
            case 4 -> desactivar(empleado);
            case 5 -> clienteController.registrar();
            case 6 -> clienteController.listar();
            case 7 -> clienteController.actualizar();
            case 8 -> clienteController.desactivar();
            case 9 -> prestamoController.radicar(empleado);
            case 10 -> prestamoController.gestionarPendientes(empleado);
            case 11 -> prestamoController.listar();
            case 12 -> prestamoController.filtrarPorRadicador();
            case 13 -> prestamoController.consultarHistorialAprobacion();
            case 14 -> pagoController.registrar(empleado);
            case 15 -> pagoController.listarPorPrestamo();
            case 16 -> reporteController.menu();
            case 17 -> respaldoController.menu();
            case 18 -> mensajes.informativo("Sesion cerrada.");
            default -> mensajes.error("Opcion invalida.");
        }
    }

    private void ejecutarMenuEmpleado(int opcion, Empleado empleado) {
        switch (opcion) {
            case 1 -> clienteController.registrar();
            case 2 -> clienteController.listar();
            case 3 -> clienteController.actualizar();
            case 4 -> prestamoController.radicar(empleado);
            case 5 -> prestamoController.gestionarPendientes(empleado);
            case 6 -> prestamoController.listar();
            case 7 -> prestamoController.filtrarPorRadicador();
            case 8 -> prestamoController.consultarHistorialAprobacion();
            case 9 -> pagoController.registrar(empleado);
            case 10 -> pagoController.listarPorPrestamo();
            case 11 -> mensajes.informativo("Sesion cerrada.");
            default -> mensajes.error("Opcion invalida.");
        }
    }

    private void registrar() {
        Empleado empleado = ingreso.pedirEmpleado();
        if (empleadoDAO.agregar(empleado)) {
            mensajes.exito("Empleado registrado correctamente.");
        } else {
            mensajes.error("No fue posible registrar el empleado. El documento puede estar asociado a un empleado existente.");
        }
    }

    private void listar() {
        List<Empleado> empleados = empleadoDAO.listar();
        imprimir.lista(empleados);
    }

    private void actualizar() {
        listar();
        int idEmpleado = ingreso.pedirIdEmpleado();
        Empleado empleado = empleadoDAO.buscar(idEmpleado);
        if (empleado == null) {
            mensajes.error("El empleado no existe.");
            return;
        }
        switch (ingreso.pedirCampoActualizacion()) {
            case 1 -> empleado.setNombre(ingreso.pedirNombre());
            case 2 -> empleado.setCorreo(ingreso.pedirCorreo());
            case 3 -> empleado.setClave(ingreso.pedirClave());
            case 4 -> {
                RolEmpleado nuevoRol = ingreso.pedirRol();
                if (empleado.getRol() == RolEmpleado.GERENTE && nuevoRol != RolEmpleado.GERENTE && empleadoDAO.contarGerentesActivos() <= 1) {
                    mensajes.error("No se puede cambiar el rol del unico gerente activo.");
                    return;
                }
                empleado.setRol(nuevoRol);
            }
            case 5 -> empleado.setSalario(ingreso.pedirSalario());
            case 6 -> {
                mensajes.informativo("Actualizacion cancelada.");
                return;
            }
            default -> {
                return;
            }
        }
        if (empleadoDAO.actualizar(empleado)) {
            mensajes.exito("Empleado actualizado correctamente.");
        } else {
            mensajes.error("No fue posible actualizar el empleado.");
        }
    }

    private void desactivar(Empleado sesion) {
        listar();
        int idEmpleado = ingreso.pedirIdEmpleado();
        Empleado empleado = empleadoDAO.buscar(idEmpleado);
        if (empleado == null) {
            mensajes.error("El empleado no existe.");
            return;
        }
        if (empleado.getIdPersona() == sesion.getIdPersona()) {
            mensajes.error("No puede desactivar el empleado de la sesion actual.");
            return;
        }
        if (empleado.getRol() == RolEmpleado.GERENTE && empleadoDAO.contarGerentesActivos() <= 1) {
            mensajes.error("No se puede desactivar el unico gerente activo.");
            return;
        }
        if (empleadoDAO.desactivar(idEmpleado)) {
            mensajes.exito("Empleado desactivado correctamente.");
        } else {
            mensajes.error("No fue posible desactivar el empleado.");
        }
    }
}
