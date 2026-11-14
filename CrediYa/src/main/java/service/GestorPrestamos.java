package service;

import dao.PrestamoDAO;
import database.Conexion;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.EstadoPrestamo;
import model.Prestamo;

public class GestorPrestamos {

    private final PrestamoDAO prestamoDAO = new PrestamoDAO();
    private List<Prestamo> prestamos = new ArrayList<>();

    public List<Prestamo> cargarPrestamos() {
        try (Connection conexion = Conexion.getInstancia().conectar()) {
            prestamos = prestamoDAO.listar();
            if (prestamos.isEmpty()) {
                System.out.println("No hay prestamos registrados para cargar.");
            }
            return new ArrayList<>(prestamos);
        } catch (SQLException e) {
            System.out.println("Error de conexion al cargar los prestamos: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Error al procesar los datos de los prestamos.");
        }
        prestamos = new ArrayList<>();
        return new ArrayList<>();
    }

    public List<Prestamo> filtrarPrestamosActivos(List<Prestamo> lista) {
        try {
            if (lista == null || lista.isEmpty()) {
                System.out.println("No hay prestamos disponibles para filtrar.");
                return new ArrayList<>();
            }
            return lista.stream()
                    .filter(prestamo -> prestamo.getEstado() == EstadoPrestamo.ACTIVO)
                    .toList();
        } catch (RuntimeException e) {
            System.out.println("Error al filtrar los prestamos activos.");
            return new ArrayList<>();
        }
    }

    public List<Prestamo> filtrarPrestamosVencidos(List<Prestamo> lista) {
        try {
            if (lista == null || lista.isEmpty()) {
                System.out.println("No hay prestamos disponibles para filtrar.");
                return new ArrayList<>();
            }
            LocalDate fechaActual = LocalDate.now();
            return lista.stream()
                    .filter(prestamo -> prestamo.getFechaVencimiento() != null)
                    .filter(prestamo -> prestamo.getFechaVencimiento().isBefore(fechaActual))
                    .toList();
        } catch (RuntimeException e) {
            System.out.println("Error al filtrar los prestamos vencidos.");
            return new ArrayList<>();
        }
    }

    public void mostrarResumen(List<Prestamo> lista) {
        try {
            if (lista == null || lista.isEmpty()) {
                System.out.println("No hay prestamos para generar el resumen.");
                return;
            }
            long activos = lista.stream()
                    .filter(prestamo -> prestamo.getEstado() == EstadoPrestamo.ACTIVO)
                    .count();
            long vencidos = lista.stream()
                    .filter(prestamo -> prestamo.getEstado() == EstadoPrestamo.VENCIDO)
                    .count();
            double totalAdeudado = redondear(lista.stream()
                    .filter(prestamo -> prestamo.getEstado() == EstadoPrestamo.ACTIVO || prestamo.getEstado() == EstadoPrestamo.VENCIDO)
                    .mapToDouble(Prestamo::getSaldoPendiente)
                    .sum());
            System.out.println("Resumen de prestamos");
            System.out.println("Prestamos activos: " + activos);
            System.out.println("Prestamos vencidos: " + vencidos);
            System.out.printf("Monto total adeudado: $%,.2f%n", totalAdeudado);
        } catch (RuntimeException e) {
            System.out.println("Error al generar el resumen de prestamos.");
        }
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
