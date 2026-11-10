package confing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://72.167.84.254:3306/banco";
    private static final String USER = "samuel";
    private static final String PASSWORD = "#3Programacion3";

    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos remota.");
        } catch (ClassNotFoundException e) {
            System.err.println("Error al cargar el driver MySQL: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error de conexión con la BD remota: " + e.getMessage());
        }
        return con;
    }

    public static void main(String[] args) {
        Connection test = Conexion.getConnection();
        if (test != null) {
            try {
                test.close();
                System.out.println("Conexión cerrada correctamente.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}