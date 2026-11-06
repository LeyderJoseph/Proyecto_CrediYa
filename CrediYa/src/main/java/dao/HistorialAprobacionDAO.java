package dao;

import database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.AccionAprobacion;
import model.Empleado;
import model.HistorialAprobacion;
import model.Prestamo;

public class HistorialAprobacionDAO {

    public boolean agregar(Connection con, Prestamo prestamo, Empleado empleado, AccionAprobacion accion, String observacion) throws SQLException {
        String sql = "INSERT INTO historial_aprobacion(id_prestamo, id_empleado, rol_empleado, accion, observacion) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, prestamo.getIdPrestamo());
            ps.setInt(2, empleado.getIdPersona());
            ps.setString(3, empleado.getRol().name());
            ps.setString(4, accion.name());
            ps.setString(5, observacion);
            return ps.executeUpdate() > 0;
        }
    }

    public List<HistorialAprobacion> listarPorPrestamo(int idPrestamo) {
        try (Connection con = Conexion.getInstancia().conectar()) {
            return listarPorPrestamo(con, idPrestamo);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    private List<HistorialAprobacion> listarPorPrestamo(Connection con, int idPrestamo) throws SQLException {
        String sql = "SELECT * FROM historial_aprobacion WHERE id_prestamo = ? ORDER BY fecha_accion";
        List<HistorialAprobacion> historial = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPrestamo);
            try (ResultSet rs = ps.executeQuery()) {
                Prestamo prestamo = new PrestamoDAO().buscar(idPrestamo);
                while (rs.next() && prestamo != null) {
                    Empleado empleado = new EmpleadoDAO().buscar(rs.getInt("id_empleado"));
                    Timestamp fecha = rs.getTimestamp("fecha_accion");
                    if (empleado != null) {
                        historial.add(new HistorialAprobacion(
                                rs.getInt("id_historial"),
                                prestamo,
                                empleado,
                                empleado.getRol(),
                                AccionAprobacion.valueOf(rs.getString("accion")),
                                fecha == null ? null : fecha.toLocalDateTime(),
                                rs.getString("observacion")
                        ));
                    }
                }
            }
        }
        return historial;
    }
}
