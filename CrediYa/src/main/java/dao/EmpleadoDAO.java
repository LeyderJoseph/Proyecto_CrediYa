package dao;

import database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.Empleado;
import model.RolEmpleado;

public class EmpleadoDAO {

    private final Conexion conexion = Conexion.getInstancia();
    private final PersonaDAO personaDAO = new PersonaDAO();

    public boolean agregar(Empleado empleado) {
        String sql = "INSERT INTO empleado(id_persona, rol, salario) VALUES (?, ?, ?)";
        try (Connection con = conexion.conectar()) {
            con.setAutoCommit(false);
            int idPersona = personaDAO.buscarIdPorDocumento(con, empleado.getDocumento());
            if (idPersona == 0) {
                idPersona = personaDAO.agregar(con, empleado);
            }
            if (existePerfil(con, idPersona)) {
                con.rollback();
                return false;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idPersona);
                ps.setString(2, empleado.getRol().name());
                ps.setDouble(3, empleado.getSalario());
                ps.executeUpdate();
            }
            empleado.setIdPersona(idPersona);
            con.commit();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public Empleado buscar(int idPersona) {
        String sql = "SELECT p.*, e.rol, e.salario FROM persona p INNER JOIN empleado e ON p.id_persona = e.id_persona WHERE p.id_persona = ?";
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPersona);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return crearEmpleado(rs);
                }
            }
        } catch (SQLException e) {
        }
        return null;
    }

    public Empleado buscarPorCredenciales(String documento, String clave) {
        String sql = "SELECT p.*, e.rol, e.salario FROM persona p INNER JOIN empleado e ON p.id_persona = e.id_persona WHERE p.documento = ? AND p.clave = ? AND p.activo = TRUE";
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, documento);
            ps.setString(2, clave);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return crearEmpleado(rs);
                }
            }
        } catch (SQLException e) {
        }
        return null;
    }

    public List<Empleado> listar() {
        String sql = "SELECT p.*, e.rol, e.salario FROM persona p INNER JOIN empleado e ON p.id_persona = e.id_persona ORDER BY p.nombre";
        List<Empleado> empleados = new ArrayList<>();
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                empleados.add(crearEmpleado(rs));
            }
        } catch (SQLException e) {
        }
        return empleados;
    }

    public boolean actualizar(Empleado empleado) {
        String sqlEmpleado = "UPDATE empleado SET rol = ?, salario = ? WHERE id_persona = ?";
        try (Connection con = conexion.conectar()) {
            con.setAutoCommit(false);
            if (!personaDAO.actualizar(con, empleado)) {
                con.rollback();
                return false;
            }
            try (PreparedStatement ps = con.prepareStatement(sqlEmpleado)) {
                ps.setString(1, empleado.getRol().name());
                ps.setDouble(2, empleado.getSalario());
                ps.setInt(3, empleado.getIdPersona());
                ps.executeUpdate();
            }
            con.commit();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean desactivar(int idPersona) {
        String sql = "UPDATE persona SET activo = FALSE WHERE id_persona = ?";
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPersona);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public int contarGerentesActivos() {
        String sql = "SELECT COUNT(*) FROM empleado e INNER JOIN persona p ON e.id_persona = p.id_persona WHERE e.rol = 'GERENTE' AND p.activo = TRUE";
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
        }
        return 0;
    }

    private boolean existePerfil(Connection con, int idPersona) throws SQLException {
        String sql = "SELECT id_persona FROM empleado WHERE id_persona = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPersona);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private Empleado crearEmpleado(ResultSet rs) throws SQLException {
        Timestamp fecha = rs.getTimestamp("fecha_registro");
        return new Empleado(
                rs.getInt("id_persona"),
                rs.getString("nombre"),
                rs.getString("documento"),
                rs.getString("correo"),
                rs.getString("clave"),
                rs.getBoolean("activo"),
                fecha == null ? null : fecha.toLocalDateTime(),
                RolEmpleado.valueOf(rs.getString("rol")),
                rs.getDouble("salario")
        );
    }
}
