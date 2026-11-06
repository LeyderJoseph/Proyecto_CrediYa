package dao;

import database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.DetallePago;
import model.Empleado;
import model.EstadoPrestamo;
import model.Pago;
import model.Prestamo;
import model.MetodoPago;
import model.TipoTransferencia;

public class PagoDAO {

    private final Conexion conexion = Conexion.getInstancia();
    private final PrestamoDAO prestamoDAO = new PrestamoDAO();
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    public boolean registrar(Pago pago) {
        String sqlPrestamo = "SELECT saldo_pendiente, estado FROM prestamo WHERE id_prestamo = ? FOR UPDATE";
        String sqlPago = "INSERT INTO pago(id_prestamo, id_empleado_registrador, valor_total, observacion) VALUES (?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_pago(id_pago, metodo_pago, tipo_transferencia, valor, referencia) VALUES (?, ?, ?, ?, ?)";
        String sqlActualizar = "UPDATE prestamo SET saldo_pendiente = ?, estado = ? WHERE id_prestamo = ?";
        try (Connection con = conexion.conectar()) {
            con.setAutoCommit(false);
            double saldo;
            EstadoPrestamo estado;
            try (PreparedStatement ps = con.prepareStatement(sqlPrestamo)) {
                ps.setInt(1, pago.getPrestamo().getIdPrestamo());
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        con.rollback();
                        return false;
                    }
                    saldo = rs.getDouble("saldo_pendiente");
                    estado = EstadoPrestamo.valueOf(rs.getString("estado"));
                }
            }
            if ((estado != EstadoPrestamo.ACTIVO && estado != EstadoPrestamo.VENCIDO) || pago.getValorTotal() <= 0 || pago.getValorTotal() > saldo || !detallesValidos(pago)) {
                con.rollback();
                return false;
            }
            try (PreparedStatement ps = con.prepareStatement(sqlPago, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, pago.getPrestamo().getIdPrestamo());
                ps.setInt(2, pago.getEmpleadoRegistrador().getIdPersona());
                ps.setDouble(3, pago.getValorTotal());
                ps.setString(4, pago.getObservacion());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) {
                        con.rollback();
                        return false;
                    }
                    pago.setIdPago(rs.getInt(1));
                }
            }
            try (PreparedStatement ps = con.prepareStatement(sqlDetalle)) {
                for (DetallePago detalle : pago.getDetalles()) {
                    ps.setInt(1, pago.getIdPago());
                    ps.setString(2, detalle.getMetodoPago().name());
                    if (detalle.getTipoTransferencia() == null) {
                        ps.setNull(3, java.sql.Types.VARCHAR);
                    } else {
                        ps.setString(3, detalle.getTipoTransferencia().name());
                    }
                    ps.setDouble(4, detalle.getValor());
                    ps.setString(5, detalle.getReferencia());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            double nuevoSaldo = redondear(saldo - pago.getValorTotal());
            EstadoPrestamo nuevoEstado = nuevoSaldo == 0 ? EstadoPrestamo.PAGADO : estado;
            try (PreparedStatement ps = con.prepareStatement(sqlActualizar)) {
                ps.setDouble(1, nuevoSaldo);
                ps.setString(2, nuevoEstado.name());
                ps.setInt(3, pago.getPrestamo().getIdPrestamo());
                ps.executeUpdate();
            }
            con.commit();
            pago.getPrestamo().setSaldoPendiente(nuevoSaldo);
            pago.getPrestamo().setEstado(nuevoEstado);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Pago> listarPorPrestamo(int idPrestamo) {
        String sql = "SELECT * FROM pago WHERE id_prestamo = ? ORDER BY fecha_pago DESC";
        List<Pago> pagos = new ArrayList<>();
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPrestamo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Prestamo prestamo = prestamoDAO.buscar(rs.getInt("id_prestamo"));
                    Empleado empleado = empleadoDAO.buscar(rs.getInt("id_empleado_registrador"));
                    Timestamp fecha = rs.getTimestamp("fecha_pago");
                    if (prestamo != null && empleado != null) {
                        Pago pago = new Pago(
                                rs.getInt("id_pago"),
                                prestamo,
                                empleado,
                                fecha == null ? null : fecha.toLocalDateTime(),
                                rs.getDouble("valor_total"),
                                rs.getString("observacion")
                        );
                        pago.setDetalles(listarDetalles(con, pago.getIdPago()));
                        pagos.add(pago);
                    }
                }
            }
        } catch (SQLException e) {
        }
        return pagos;
    }

    public List<Pago> listarTodos() {
        String sql = "SELECT * FROM pago ORDER BY fecha_pago DESC";
        List<Pago> pagos = new ArrayList<>();
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Prestamo prestamo = prestamoDAO.buscar(rs.getInt("id_prestamo"));
                Empleado empleado = empleadoDAO.buscar(rs.getInt("id_empleado_registrador"));
                Timestamp fecha = rs.getTimestamp("fecha_pago");
                if (prestamo != null && empleado != null) {
                    Pago pago = new Pago(
                            rs.getInt("id_pago"),
                            prestamo,
                            empleado,
                            fecha == null ? null : fecha.toLocalDateTime(),
                            rs.getDouble("valor_total"),
                            rs.getString("observacion")
                    );
                    pago.setDetalles(listarDetalles(con, pago.getIdPago()));
                    pagos.add(pago);
                }
            }
        } catch (SQLException e) {
        }
        return pagos;
    }

    private List<DetallePago> listarDetalles(Connection con, int idPago) throws SQLException {
        String sql = "SELECT * FROM detalle_pago WHERE id_pago = ?";
        List<DetallePago> detalles = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPago);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String tipo = rs.getString("tipo_transferencia");
                    detalles.add(new DetallePago(
                            rs.getInt("id_detalle_pago"),
                            MetodoPago.valueOf(rs.getString("metodo_pago")),
                            tipo == null ? null : TipoTransferencia.valueOf(tipo),
                            rs.getDouble("valor"),
                            rs.getString("referencia")
                    ));
                }
            }
        }
        return detalles;
    }

    private boolean detallesValidos(Pago pago) {
        if (pago.getDetalles().isEmpty()) {
            return false;
        }
        double suma = 0;
        for (DetallePago detalle : pago.getDetalles()) {
            if (detalle.getValor() <= 0) {
                return false;
            }
            if (detalle.getMetodoPago() == MetodoPago.EFECTIVO && detalle.getTipoTransferencia() != null) {
                return false;
            }
            if (detalle.getMetodoPago() == MetodoPago.TRANSFERENCIA && (detalle.getTipoTransferencia() == null || detalle.getReferencia() == null || detalle.getReferencia().isBlank())) {
                return false;
            }
            suma += detalle.getValor();
        }
        return Math.abs(redondear(suma) - redondear(pago.getValorTotal())) < 0.01;
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
