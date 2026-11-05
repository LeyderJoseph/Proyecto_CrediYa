package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static Conexion instancia;
    private final String url = "jdbc:mysql://localhost:3306/crediya_db";
    private final String usuario = "root";
    private final String clave = "alisus10";

    private Conexion() {
    }

    public static Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    public Connection conectar() throws SQLException {
        return DriverManager.getConnection(url, usuario, clave);
    }
}
