package dao;

import database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.Persona;

public class PersonaDAO {

    private final Conexion conexion = Conexion.getInstancia();

    public int buscarIdPorDocumento(Connection con, String documento) throws SQLException {
        String sql = "SELECT id_persona FROM persona WHERE documento = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, documento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_persona");
                }
            }
        }
        return 0;
    }

    public boolean existeDocumentoOCorreo(String documento, String correo) {
        String sql = "SELECT id_persona FROM persona WHERE documento = ? OR correo = ?";
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, documento);
            ps.setString(2, correo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return true;
        }
    }

    public int agregar(Connection con, Persona persona) throws SQLException {
        String sql = "INSERT INTO persona(nombre, documento, correo, clave, activo) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, persona.getNombre());
            ps.setString(2, persona.getDocumento());
            ps.setString(3, persona.getCorreo());
            ps.setString(4, persona.getClave());
            ps.setBoolean(5, persona.isActivo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    persona.setIdPersona(id);
                    return id;
                }
            }
        }
        return 0;
    }

    public boolean actualizar(Persona persona) {
        String sql = "UPDATE persona SET nombre = ?, correo = ?, clave = ?, activo = ? WHERE id_persona = ?";
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            return actualizar(con, persona);
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean actualizar(Connection con, Persona persona) throws SQLException {
        String sql = "UPDATE persona SET nombre = ?, correo = ?, clave = ?, activo = ? WHERE id_persona = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, persona.getNombre());
            ps.setString(2, persona.getCorreo());
            ps.setString(3, persona.getClave());
            ps.setBoolean(4, persona.isActivo());
            ps.setInt(5, persona.getIdPersona());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean existeGerenteActivo() {
        String sql = "SELECT e.id_persona FROM empleado e INNER JOIN persona p ON e.id_persona = p.id_persona WHERE e.rol = 'GERENTE' AND p.activo = TRUE";
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }
}
