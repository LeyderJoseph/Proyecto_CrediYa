package controller;

import dao.PagoDAO;
import dao.PrestamoDAO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.DetallePago;
import model.Cliente;
import model.Empleado;
import model.EstadoPrestamo;
import model.Pago;
import model.Prestamo;
import view.ingreso.IngresoPago;
import view.imprimir.ImprimirPago;
import view.imprimir.ImprimirPrestamo;
import view.imprimir.Mensajes;

public class PagoController {

    private final PagoDAO pagoDAO = new PagoDAO();
    private final PrestamoDAO prestamoDAO = new PrestamoDAO();
    private final IngresoPago ingreso = new IngresoPago();
    private final ImprimirPago imprimir = new ImprimirPago();
    private final ImprimirPrestamo imprimirPrestamo = new ImprimirPrestamo();
    private final Mensajes mensajes = new Mensajes();

    public void registrar(Empleado empleado) {
        imprimirPrestamo.lista(prestamoDAO.listar());
        int idPrestamo = ingreso.pedirIdPrestamo();
        Prestamo prestamo = prestamoDAO.buscar(idPrestamo);
        if (prestamo == null) {
            mensajes.error("El prestamo no existe.");
            return;
        }
        if (prestamo.getEstado() != EstadoPrestamo.ACTIVO && prestamo.getEstado() != EstadoPrestamo.VENCIDO) {
            mensajes.error("Solo se pueden registrar pagos para prestamos activos o vencidos.");
            return;
        }
        double total = ingreso.pedirValorTotal(prestamo.getSaldoPendiente());
        List<DetallePago> detalles = new ArrayList<>();
        double acumulado = 0;
        while (acumulado < total) {
            double restante = redondear(total - acumulado);
            DetallePago detalle = ingreso.pedirDetalle(restante);
            detalles.add(detalle);
            acumulado = redondear(acumulado + detalle.getValor());
        }
        Pago pago = new Pago(0, prestamo, empleado, LocalDateTime.now(), total, ingreso.pedirObservacion());
        pago.setDetalles(detalles);
        if (pagoDAO.registrar(pago)) {
            mensajes.exito("Pago registrado. Saldo pendiente: " + pago.getPrestamo().getSaldoPendiente());
        } else {
            mensajes.error("No fue posible registrar el pago.");
        }
    }

    public void listarPorPrestamo() {
        imprimirPrestamo.lista(prestamoDAO.listar());
        int idPrestamo = ingreso.pedirIdPrestamo();
        imprimir.lista(pagoDAO.listarPorPrestamo(idPrestamo));
    }

    public void listarPorPrestamo(int idPrestamo) {
        imprimir.lista(pagoDAO.listarPorPrestamo(idPrestamo));
    }

    public void listarPorPrestamoCliente(Cliente cliente) {
        imprimirPrestamo.lista(prestamoDAO.listarPorCliente(cliente.getIdPersona()));
        int idPrestamo = ingreso.pedirIdPrestamo();
        Prestamo prestamo = prestamoDAO.buscar(idPrestamo);
        if (prestamo == null || prestamo.getCliente().getIdPersona() != cliente.getIdPersona()) {
            mensajes.error("El prestamo no pertenece al cliente autenticado.");
            return;
        }
        imprimir.lista(pagoDAO.listarPorPrestamo(idPrestamo));
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
