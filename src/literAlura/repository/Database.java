package literAlura.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:literAlura.db";


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void inicializar() {

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = """
                CREATE TABLE IF NOT EXISTS libros (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    titulo TEXT NOT NULL,
                    autor TEXT NOT NULL,
                    idioma TEXT NOT NULL,
                    fecha_guardada TEXT NOT NULL
                );
                """;

            stmt.execute(sql);
            System.out.println("✅ Base de datos inicializada correctamente.");
        } catch (SQLException e) {
            System.out.println("❌ Error al inicializar la base de datos: " + e.getMessage());


        }


    }
}

