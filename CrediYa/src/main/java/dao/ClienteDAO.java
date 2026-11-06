package dao;

import database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;

public class ClienteDAO {

    private final Conexion conexion = Conexion.getInstancia();
    private final PersonaDAO personaDAO = new PersonaDAO();

    public boolean agregar(Cliente cliente) {
        String sql = "INSERT INTO cliente(id_persona, telefono) VALUES (?, ?)";
        try (Connection con = conexion.conectar()) {
            con.setAutoCommit(false);
            int idPersona = personaDAO.buscarIdPorDocumento(con, cliente.getDocumento());
            if (idPersona == 0) {
                idPersona = personaDAO.agregar(con, cliente);
            }
            if (existePerfil(con, idPersona)) {
                con.rollback();
                return false;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idPersona);
                ps.setString(2, cliente.getTelefono());
                ps.executeUpdate();
            }
            cliente.setIdPersona(idPersona);
            con.commit();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public Cliente buscar(int idPersona) {
        String sql = "SELECT p.*, c.telefono FROM persona p INNER JOIN cliente c ON p.id_persona = c.id_persona WHERE p.id_persona = ?";
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPersona);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return crearCliente(rs);
                }
            }
        } catch (SQLException e) {
        }
        return null;
    }

    public Cliente buscarPorCredenciales(String documento, String clave) {
        String sql = "SELECT p.*, c.telefono FROM persona p INNER JOIN cliente c ON p.id_persona = c.id_persona WHERE p.documento = ? AND p.clave = ? AND p.activo = TRUE";
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, documento);
            ps.setString(2, clave);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return crearCliente(rs);
                }
            }
        } catch (SQLException e) {
        }
        return null;
    }

    public List<Cliente> listar() {
        String sql = "SELECT p.*, c.telefono FROM persona p INNER JOIN cliente c ON p.id_persona = c.id_persona ORDER BY p.nombre";
        List<Cliente> clientes = new ArrayList<>();
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clientes.add(crearCliente(rs));
            }
        } catch (SQLException e) {
        }
        return clientes;
    }

    public boolean actualizar(Cliente cliente) {
        String sqlCliente = "UPDATE cliente SET telefono = ? WHERE id_persona = ?";
        try (Connection con = conexion.conectar()) {
            con.setAutoCommit(false);
            if (!personaDAO.actualizar(con, cliente)) {
                con.rollback();
                return false;
            }
            try (PreparedStatement ps = con.prepareStatement(sqlCliente)) {
                ps.setString(1, cliente.getTelefono());
                ps.setInt(2, cliente.getIdPersona());
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

    public boolean tienePrestamosActivosOVencidos(int idPersona) {
        String sql = "SELECT id_prestamo FROM prestamo WHERE id_cliente = ? AND estado IN ('ACTIVO', 'VENCIDO')";
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPersona);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return true;
        }
    }

    private boolean existePerfil(Connection con, int idPersona) throws SQLException {
        String sql = "SELECT id_persona FROM cliente WHERE id_persona = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPersona);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private Cliente crearCliente(ResultSet rs) throws SQLException {
        Timestamp fecha = rs.getTimestamp("fecha_registro");
        return new Cliente(
                rs.getInt("id_persona"),
                rs.getString("nombre"),
                rs.getString("documento"),
                rs.getString("correo"),
                rs.getString("clave"),
                rs.getBoolean("activo"),
                fecha == null ? null : fecha.toLocalDateTime(),
                rs.getString("telefono")
        );
    }
}
