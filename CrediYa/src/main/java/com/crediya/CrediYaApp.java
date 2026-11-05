package com.crediya;

import controller.AutenticacionController;
import controller.ClienteController;
import controller.EmpleadoController;
import controller.PagoController;
import controller.PrestamoController;
import database.Conexion;
import java.sql.Connection;
import java.sql.SQLException;
import model.Empleado;
import model.Cliente;
import view.ingreso.IngresoLogin;
import view.ingreso.IngresoEmpleado;
import view.imprimir.MenuCliente;
import view.imprimir.MenuPrincipal;
import view.imprimir.Mensajes;

public class CrediYaApp {

    public static void main(String[] args) {
        System.out.println("CrediYa S.A.S.");
        if (probarConexion()) {
            registrarGerenteInicial();
            iniciarAplicacion();
        }
    }

    private static boolean probarConexion() {
        try (Connection conexion = Conexion.getInstancia().conectar()) {
            System.out.println("Conexion a MySQL exitosa.");
            return true;
        } catch (SQLException e) {
            System.out.println("No fue posible conectar con MySQL: " + e.getMessage());
            return false;
        }
    }

    private static void registrarGerenteInicial() {
        AutenticacionController autenticacion = new AutenticacionController();
        if (!autenticacion.requiereRegistroInicial()) {
            System.out.println("Ya existe un gerente activo.");
            return;
        }
        Mensajes mensajes = new Mensajes();
        IngresoEmpleado ingreso = new IngresoEmpleado();
        mensajes.informativo("Registro inicial del gerente");
        Empleado gerente = ingreso.pedirGerenteInicial();
        if (autenticacion.registrarGerenteInicial(gerente)) {
            mensajes.exito("Gerente registrado correctamente.");
        } else {
            mensajes.error("No fue posible registrar el gerente.");
        }
    }

    private static void iniciarAplicacion() {
        AutenticacionController autenticacion = new AutenticacionController();
        MenuPrincipal menu = new MenuPrincipal();
        IngresoLogin ingreso = new IngresoLogin();
        Mensajes mensajes = new Mensajes();
        int opcion;
        do {
            opcion = menu.menu();
            if (opcion == 1) {
                String documento = ingreso.pedirDocumento();
                String clave = ingreso.pedirClave();
                Empleado empleado = autenticacion.autenticarEmpleado(documento, clave);
                Cliente cliente = autenticacion.autenticarCliente(documento, clave);
                abrirSesion(menu, mensajes, empleado, cliente);
            }
        } while (opcion != 2);
        mensajes.informativo("Aplicacion finalizada.");
    }

    private static void abrirSesion(MenuPrincipal menu, Mensajes mensajes, Empleado empleado, Cliente cliente) {
        if (empleado == null && cliente == null) {
            mensajes.error("Documento, clave o estado de usuario invalido.");
            return;
        }
        if (empleado != null && cliente != null) {
            if (menu.seleccionarPerfil() == 1) {
                new EmpleadoController().menu(empleado);
            } else {
                abrirMenuCliente(cliente);
            }
            return;
        }
        if (empleado != null) {
            new EmpleadoController().menu(empleado);
            return;
        }
        abrirMenuCliente(cliente);
    }

    private static void abrirMenuCliente(Cliente cliente) {
        ClienteController controller = new ClienteController();
        PrestamoController prestamoController = new PrestamoController();
        PagoController pagoController = new PagoController();
        MenuCliente menu = new MenuCliente();
        int opcion;
        do {
            opcion = menu.menu();
            switch (opcion) {
                case 1 -> controller.mostrarInformacion(cliente);
                case 2 -> prestamoController.listarPorCliente(cliente);
                case 3 -> prestamoController.filtrarPorCliente(cliente);
                case 4 -> pagoController.listarPorPrestamoCliente(cliente);
                case 5 -> System.out.println("Sesion cerrada.");
                default -> System.out.println("Opcion invalida.");
            }
        } while (opcion != 5);
    }
}
