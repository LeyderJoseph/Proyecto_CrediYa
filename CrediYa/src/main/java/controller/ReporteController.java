package controller;

import dao.PagoDAO;
import dao.PrestamoDAO;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import model.Cliente;
import model.EstadoPrestamo;
import model.Prestamo;
import service.GestorPrestamos;
import view.imprimir.ImprimirPrestamo;
import view.imprimir.MenuReporte;

public class ReporteController {

    private final PrestamoDAO prestamoDAO = new PrestamoDAO();
    private final PagoDAO pagoDAO = new PagoDAO();
    private final ImprimirPrestamo imprimir = new ImprimirPrestamo();
    private final MenuReporte menu = new MenuReporte();
    private final GestorPrestamos gestorPrestamos = new GestorPrestamos();

    public void menu() {
        int opcion;
        do {
            opcion = menu.menu();
            prestamoDAO.actualizarVencidos();
            switch (opcion) {
                case 1 -> imprimir.lista(prestamosPorEstado(EstadoPrestamo.ACTIVO));
                case 2 -> imprimir.lista(prestamosPorEstado(EstadoPrestamo.VENCIDO));
                case 3 -> mostrarMorosos();
                case 4 -> System.out.println("Total prestado: " + totalPrestado());
                case 5 -> System.out.println("Total recaudado: " + totalRecaudado());
                case 6 -> System.out.println("Saldo total de cartera: " + saldoCartera());
                case 7 -> gestorPrestamos.mostrarResumen(gestorPrestamos.cargarPrestamos());
                case 8 -> System.out.println("Regresando al menu gerente.");
                default -> System.out.println("Opcion invalida.");
            }
        } while (opcion != 8);
    }

    private List<Prestamo> prestamosPorEstado(EstadoPrestamo estado) {
        return prestamoDAO.listar().stream()
                .filter(prestamo -> prestamo.getEstado() == estado)
                .toList();
    }

    private void mostrarMorosos() {
        Map<Integer, Cliente> morosos = prestamosPorEstado(EstadoPrestamo.VENCIDO).stream()
                .collect(Collectors.toMap(
                        prestamo -> prestamo.getCliente().getIdPersona(),
                        Prestamo::getCliente,
                        (primero, segundo) -> primero
                ));
        if (morosos.isEmpty()) {
            System.out.println("No hay clientes morosos.");
            return;
        }
        morosos.values().forEach(cliente -> System.out.println("Cliente moroso: " + cliente.getNombre() + " | Documento: " + cliente.getDocumento()));
    }

    private double totalPrestado() {
        return redondear(prestamoDAO.listar().stream()
                .filter(prestamo -> prestamo.getEstado() != EstadoPrestamo.RECHAZADO)
                .mapToDouble(Prestamo::getCapital)
                .sum());
    }

    private double totalRecaudado() {
        return redondear(pagoDAO.listarTodos().stream()
                .mapToDouble(pago -> pago.getValorTotal())
                .sum());
    }

    private double saldoCartera() {
        return redondear(prestamoDAO.listar().stream()
                .filter(prestamo -> prestamo.getEstado() == EstadoPrestamo.ACTIVO || prestamo.getEstado() == EstadoPrestamo.VENCIDO)
                .mapToDouble(Prestamo::getSaldoPendiente)
                .sum());
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
