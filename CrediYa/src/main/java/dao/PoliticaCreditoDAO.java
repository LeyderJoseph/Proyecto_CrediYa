package dao;

import database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.PoliticaCredito;

public class PoliticaCreditoDAO {

    private final Conexion conexion = Conexion.getInstancia();

    public PoliticaCredito obtenerActiva() {
        String sql = "SELECT * FROM politica_credito WHERE activo = TRUE LIMIT 1";
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new PoliticaCredito(
                        rs.getInt("id_politica"),
                        rs.getDouble("tasa_interes_mensual"),
                        rs.getDouble("monto_minimo"),
                        rs.getDouble("monto_maximo"),
                        rs.getInt("plazo_minimo_meses"),
                        rs.getInt("plazo_maximo_meses"),
                        rs.getDouble("tope_asesor"),
                        rs.getDouble("tope_supervisor"),
                        rs.getBoolean("activo")
                );
            }
        } catch (SQLException e) {
        }
        return null;
    }
}
