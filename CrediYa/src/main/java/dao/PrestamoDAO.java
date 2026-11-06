package dao;

import database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.AccionAprobacion;
import model.Cliente;
import model.Empleado;
import model.EstadoPrestamo;
import model.Prestamo;

public class PrestamoDAO {

    private final Conexion conexion = Conexion.getInstancia();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private final HistorialAprobacionDAO historialDAO = new HistorialAprobacionDAO();

    public boolean agregar(Prestamo prestamo) {
        String sql = "INSERT INTO prestamo(id_cliente, id_empleado_radicador, capital, tipo_interes, tasa_interes_mensual, plazo_meses, interes_total, monto_total, valor_cuota, saldo_pendiente, fecha_vencimiento, estado, observacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = conexion.conectar()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, prestamo.getCliente().getIdPersona());
                ps.setInt(2, prestamo.getEmpleadoRadicador().getIdPersona());
                ps.setDouble(3, prestamo.getCapital());
                ps.setString(4, prestamo.getTipoInteres());
                ps.setDouble(5, prestamo.getTasaInteresMensual());
                ps.setInt(6, prestamo.getPlazoMeses());
                ps.setDouble(7, prestamo.getInteresTotal());
                ps.setDouble(8, prestamo.getMontoTotal());
                ps.setDouble(9, prestamo.getValorCuota());
                ps.setDouble(10, prestamo.getSaldoPendiente());
                ps.setObject(11, prestamo.getFechaVencimiento());
                ps.setString(12, prestamo.getEstado().name());
                ps.setString(13, prestamo.getObservacion());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) {
                        con.rollback();
                        return false;
                    }
                    prestamo.setIdPrestamo(rs.getInt(1));
                }
            }
            historialDAO.agregar(con, prestamo, prestamo.getEmpleadoRadicador(), AccionAprobacion.RADICADO, "Solicitud de prestamo registrada.");
            con.commit();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean registrarEscalamiento(Prestamo prestamo, Empleado empleado) {
        try (Connection con = conexion.conectar()) {
            return historialDAO.agregar(con, prestamo, empleado, AccionAprobacion.ESCALADO, "Solicitud escalada al siguiente nivel de aprobacion.");
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean decidir(Prestamo prestamo, Empleado aprobador, boolean aprobar, String observacion) {
        String sql = "UPDATE prestamo SET id_empleado_aprobador = ?, fecha_aprobacion = CURRENT_TIMESTAMP, estado = ?, observacion = ? WHERE id_prestamo = ? AND estado = 'PENDIENTE'";
        EstadoPrestamo estado = aprobar ? EstadoPrestamo.ACTIVO : EstadoPrestamo.RECHAZADO;
        AccionAprobacion accion = aprobar ? AccionAprobacion.APROBADO : AccionAprobacion.RECHAZADO;
        try (Connection con = conexion.conectar()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, aprobador.getIdPersona());
                ps.setString(2, estado.name());
                ps.setString(3, observacion);
                ps.setInt(4, prestamo.getIdPrestamo());
                if (ps.executeUpdate() == 0) {
                    con.rollback();
                    return false;
                }
            }
            historialDAO.agregar(con, prestamo, aprobador, accion, observacion);
            con.commit();
            prestamo.setEmpleadoAprobador(aprobador);
            prestamo.setEstado(estado);
            prestamo.setFechaAprobacion(LocalDateTime.now());
            prestamo.setObservacion(observacion);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Prestamo> listar() {
        return listarPorConsulta("SELECT * FROM prestamo ORDER BY fecha_solicitud DESC");
    }

    public List<Prestamo> listarPendientes() {
        return listarPorConsulta("SELECT * FROM prestamo WHERE estado = 'PENDIENTE' ORDER BY fecha_solicitud");
    }

    public List<Prestamo> listarPorCliente(int idCliente) {
        String sql = "SELECT * FROM prestamo WHERE id_cliente = ? ORDER BY fecha_solicitud DESC";
        List<Prestamo> prestamos = new ArrayList<>();
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Prestamo prestamo = crearPrestamo(rs);
                    if (prestamo != null) {
                        prestamos.add(prestamo);
                    }
                }
            }
        } catch (SQLException e) {
        }
        return prestamos;
    }

    public List<Prestamo> listarPorRadicador(int idEmpleado) {
        String sql = "SELECT * FROM prestamo WHERE id_empleado_radicador = ? ORDER BY fecha_solicitud DESC";
        List<Prestamo> prestamos = new ArrayList<>();
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Prestamo prestamo = crearPrestamo(rs);
                    if (prestamo != null) {
                        prestamos.add(prestamo);
                    }
                }
            }
        } catch (SQLException e) {
        }
        return prestamos;
    }

    public boolean actualizarVencidos() {
        String sql = "UPDATE prestamo SET estado = 'VENCIDO' WHERE estado = 'ACTIVO' AND fecha_vencimiento < CURDATE() AND saldo_pendiente > 0";
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public Prestamo buscar(int idPrestamo) {
        String sql = "SELECT * FROM prestamo WHERE id_prestamo = ?";
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPrestamo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return crearPrestamo(rs);
                }
            }
        } catch (SQLException e) {
        }
        return null;
    }

    private List<Prestamo> listarPorConsulta(String sql) {
        List<Prestamo> prestamos = new ArrayList<>();
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Prestamo prestamo = crearPrestamo(rs);
                    if (prestamo != null) {
                        prestamos.add(prestamo);
                    }
                }
            }
        } catch (SQLException e) {
        }
        return prestamos;
    }

    private Prestamo crearPrestamo(ResultSet rs) throws SQLException {
        Cliente cliente = clienteDAO.buscar(rs.getInt("id_cliente"));
        Empleado radicador = empleadoDAO.buscar(rs.getInt("id_empleado_radicador"));
        int idAprobador = rs.getInt("id_empleado_aprobador");
        Empleado aprobador = rs.wasNull() ? null : empleadoDAO.buscar(idAprobador);
        if (cliente == null || radicador == null) {
            return null;
        }
        Timestamp solicitud = rs.getTimestamp("fecha_solicitud");
        Timestamp aprobacion = rs.getTimestamp("fecha_aprobacion");
        LocalDate vencimiento = rs.getDate("fecha_vencimiento").toLocalDate();
        return new Prestamo(
                rs.getInt("id_prestamo"),
                cliente,
                radicador,
                aprobador,
                solicitud == null ? null : solicitud.toLocalDateTime(),
                aprobacion == null ? null : aprobacion.toLocalDateTime(),
                rs.getDouble("capital"),
                rs.getDouble("tasa_interes_mensual"),
                rs.getInt("plazo_meses"),
                rs.getDouble("interes_total"),
                rs.getDouble("monto_total"),
                rs.getDouble("valor_cuota"),
                rs.getDouble("saldo_pendiente"),
                vencimiento,
                EstadoPrestamo.valueOf(rs.getString("estado")),
                rs.getString("observacion")
        );
    }
}
