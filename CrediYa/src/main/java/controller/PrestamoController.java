package controller;

import dao.ClienteDAO;
import dao.EmpleadoDAO;
import dao.HistorialAprobacionDAO;
import dao.PoliticaCreditoDAO;
import dao.PrestamoDAO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import model.Cliente;
import model.Empleado;
import model.EstadoPrestamo;
import model.HistorialAprobacion;
import model.PoliticaCredito;
import model.Prestamo;
import model.RolEmpleado;
import service.aprobacion.AprobadorPrestamo;
import service.aprobacion.AsesorAprobador;
import service.aprobacion.GerenteAprobador;
import service.aprobacion.SupervisorAprobador;
import view.ingreso.IngresoPrestamo;
import view.imprimir.ImprimirPrestamo;
import view.imprimir.ImprimirCliente;
import view.imprimir.ImprimirEmpleado;
import view.imprimir.ImprimirHistorialAprobacion;
import view.imprimir.Mensajes;

public class PrestamoController {

    private final PrestamoDAO prestamoDAO = new PrestamoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private final HistorialAprobacionDAO historialDAO = new HistorialAprobacionDAO();
    private final PoliticaCreditoDAO politicaDAO = new PoliticaCreditoDAO();
    private final IngresoPrestamo ingreso = new IngresoPrestamo();
    private final ImprimirPrestamo imprimir = new ImprimirPrestamo();
    private final ImprimirCliente imprimirCliente = new ImprimirCliente();
    private final ImprimirEmpleado imprimirEmpleado = new ImprimirEmpleado();
    private final ImprimirHistorialAprobacion imprimirHistorial = new ImprimirHistorialAprobacion();
    private final Mensajes mensajes = new Mensajes();

    public void radicar(Empleado empleado) {
        PoliticaCredito politica = politicaDAO.obtenerActiva();
        if (politica == null) {
            mensajes.error("No existe una politica de credito activa.");
            return;
        }
        imprimirCliente.lista(clienteDAO.listar());
        int idCliente = ingreso.pedirIdCliente();
        Cliente cliente = clienteDAO.buscar(idCliente);
        if (cliente == null || !cliente.isActivo()) {
            mensajes.error("El cliente no existe o esta inactivo.");
            return;
        }
        if (cliente.getIdPersona() == empleado.getIdPersona()) {
            mensajes.error("No puede radicar su propio prestamo.");
            return;
        }
        double capital = ingreso.pedirCapital();
        if (capital < politica.getMontoMinimo() || capital > politica.getMontoMaximo()) {
            mensajes.error("El capital debe estar entre " + politica.getMontoMinimo() + " y " + politica.getMontoMaximo() + ".");
            return;
        }
        int plazo = ingreso.pedirPlazo();
        if (plazo < politica.getPlazoMinimoMeses() || plazo > politica.getPlazoMaximoMeses()) {
            mensajes.error("El plazo debe estar entre " + politica.getPlazoMinimoMeses() + " y " + politica.getPlazoMaximoMeses() + " meses.");
            return;
        }
        double tasa = politica.getTasaInteresMensual() / 100;
        double montoTotal = redondear(capital * Math.pow(1 + tasa, plazo));
        double interesTotal = redondear(montoTotal - capital);
        double valorCuota = redondear(montoTotal / plazo);
        Prestamo prestamo = new Prestamo(
                0,
                cliente,
                empleado,
                null,
                LocalDateTime.now(),
                null,
                capital,
                politica.getTasaInteresMensual(),
                plazo,
                interesTotal,
                montoTotal,
                valorCuota,
                montoTotal,
                LocalDate.now().plusMonths(plazo),
                EstadoPrestamo.PENDIENTE,
                "Solicitud pendiente de aprobacion."
        );
        if (prestamoDAO.agregar(prestamo)) {
            if (debeEscalar(prestamo, politica)) {
                prestamoDAO.registrarEscalamiento(prestamo, empleado);
            }
            mensajes.exito("Prestamo radicado con ID " + prestamo.getIdPrestamo() + ". Total: " + prestamo.getMontoTotal() + ". Cuota: " + prestamo.getValorCuota());
        } else {
            mensajes.error("No fue posible radicar el prestamo.");
        }
    }

    public void gestionarPendientes(Empleado empleado) {
        PoliticaCredito politica = politicaDAO.obtenerActiva();
        if (politica == null) {
            mensajes.error("No existe una politica de credito activa.");
            return;
        }
        List<Prestamo> pendientes = prestamoDAO.listarPendientes().stream()
                .filter(prestamo -> puedeGestionar(prestamo, empleado.getRol(), politica))
                .filter(prestamo -> prestamo.getCliente().getIdPersona() != empleado.getIdPersona())
                .toList();
        imprimir.lista(pendientes);
        if (pendientes.isEmpty()) {
            return;
        }
        int idPrestamo = ingreso.pedirIdPrestamo();
        Prestamo prestamo = pendientes.stream()
                .filter(item -> item.getIdPrestamo() == idPrestamo)
                .findFirst()
                .orElse(null);
        if (prestamo == null) {
            mensajes.error("El prestamo no esta disponible para su nivel de aprobacion.");
            return;
        }
        boolean aprobar = ingreso.pedirDecision();
        String observacion = ingreso.pedirObservacion();
        if (crearCadena().procesar(prestamo, empleado, politica, aprobar, observacion)) {
            mensajes.exito(aprobar ? "Prestamo aprobado correctamente." : "Prestamo rechazado correctamente.");
        } else {
            mensajes.error("No fue posible procesar la decision.");
        }
    }

    public void listar() {
        imprimir.lista(prestamoDAO.listar());
    }

    public void listarPorCliente(Cliente cliente) {
        imprimir.lista(prestamoDAO.listarPorCliente(cliente.getIdPersona()));
    }

    public void filtrarPorRadicador() {
        imprimirEmpleado.lista(empleadoDAO.listar());
        int idEmpleado = ingreso.pedirIdEmpleadoRadicador();
        Empleado empleado = empleadoDAO.buscar(idEmpleado);
        if (empleado == null) {
            mensajes.error("El empleado no existe.");
            return;
        }
        imprimir.lista(prestamoDAO.listarPorRadicador(idEmpleado));
    }

    public void consultarHistorialAprobacion() {
        imprimir.lista(prestamoDAO.listar());
        int idPrestamo = ingreso.pedirIdPrestamo();
        Prestamo prestamo = prestamoDAO.buscar(idPrestamo);
        if (prestamo == null) {
            mensajes.error("El prestamo no existe.");
            return;
        }
        List<HistorialAprobacion> historial = historialDAO.listarPorPrestamo(idPrestamo);
        imprimirHistorial.lista(historial);
    }

    public void filtrarPorCliente(Cliente cliente) {
        EstadoPrestamo estado = ingreso.pedirEstado();
        List<Prestamo> prestamos = prestamoDAO.listarPorCliente(cliente.getIdPersona()).stream()
                .filter(prestamo -> prestamo.getEstado() == estado)
                .toList();
        imprimir.lista(prestamos);
    }

    private AprobadorPrestamo crearCadena() {
        AprobadorPrestamo asesor = new AsesorAprobador();
        AprobadorPrestamo supervisor = new SupervisorAprobador();
        AprobadorPrestamo gerente = new GerenteAprobador();
        asesor.setSiguiente(supervisor);
        supervisor.setSiguiente(gerente);
        return asesor;
    }

    private boolean puedeGestionar(Prestamo prestamo, RolEmpleado rol, PoliticaCredito politica) {
        return switch (rol) {
            case ASESOR -> prestamo.getCapital() <= politica.getTopeAsesor();
            case SUPERVISOR -> prestamo.getCapital() > politica.getTopeAsesor() && prestamo.getCapital() <= politica.getTopeSupervisor();
            case GERENTE -> prestamo.getCapital() > politica.getTopeSupervisor() && prestamo.getCapital() <= politica.getMontoMaximo();
        };
    }

    private boolean debeEscalar(Prestamo prestamo, PoliticaCredito politica) {
        return prestamo.getCapital() > politica.getTopeAsesor();
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
