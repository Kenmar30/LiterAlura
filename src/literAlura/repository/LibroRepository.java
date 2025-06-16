package literAlura.repository;

import literAlura.model.Autor;
import literAlura.model.Libro;
import literAlura.repository.Database;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class LibroRepository {

    private static final String URL = "jdbc:sqlite:literAlura.db";

    public void guardarLibro(Libro libro) {
        String sql = "INSERT INTO libros (titulo, autor, idioma, fecha_guardada) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String titulo = libro.getTitle();

            String autor = libro.getAuthors() != null && !libro.getAuthors().isEmpty()
                    ? libro.getAuthors().get(0).getName()
                    : "Desconocido";

            String idioma = libro.getLanguages() != null && !libro.getLanguages().isEmpty()
                    ? libro.getLanguages().get(0)
                    : "desconocido";

            String fechaActual = LocalDate.now().toString(); // formato YYYY-MM-DD

            pstmt.setString(1, titulo);
            pstmt.setString(2, autor);
            pstmt.setString(3, idioma);
            pstmt.setString(4, fechaActual);

            pstmt.executeUpdate();
            System.out.println("✅ Libro guardado en la base de datos.");

        } catch (SQLException e) {
            System.out.println("⚠️ Error al guardar libro: " + e.getMessage());
        }
    }

    public boolean existeLibro(String titulo, String autor) {
        String sql = "SELECT COUNT(*) FROM libros WHERE titulo = ? AND autor = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, titulo);
            stmt.setString(2, autor);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al verificar existencia del libro: " + e.getMessage());
            return false;
        }
    }

    public List<Libro> obtenerTodos() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT titulo, autor, idioma, fecha_guardada FROM libros";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String titulo = rs.getString("titulo");
                String nombreAutor = rs.getString("autor");
                String idioma = rs.getString("idioma");
                String fechaGuardada = rs.getString("fecha_guardada");

                // Crear objeto Autor
                List<Autor> autores = new ArrayList<>();
                if (nombreAutor != null && !nombreAutor.isEmpty()) {
                    Autor autor = new Autor();
                    autor.setName(nombreAutor);
                    autores.add(autor);
                }

                List<String> idiomas = List.of(idioma);
                Libro libro = new Libro(titulo, autores, idiomas);
                libro.setFechaGuardada(fechaGuardada); // 💾 guardar la fecha

                libros.add(libro);
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Error al leer libros: " + e.getMessage());
        }

        return libros;
    }

    public int contarTotalLibros() {
        String sql = "SELECT COUNT(*) FROM libros";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("⚠️ Error al contar libros: " + e.getMessage());
            return 0;
        }
    }

    public int contarAutoresDistintos() {
        String sql = "SELECT COUNT(DISTINCT autor) FROM libros";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("⚠️ Error al contar autores distintos: " + e.getMessage());
            return 0;
        }
    }

    public Map<String, Integer> contarLibrosPorIdioma() {
        Map<String, Integer> mapa = new HashMap<>();
        String sql = "SELECT idioma, COUNT(*) AS cantidad FROM libros GROUP BY idioma";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                mapa.put(rs.getString("idioma"), rs.getInt("cantidad"));
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error al contar libros por idioma: " + e.getMessage());
        }
        return mapa;
    }

    public boolean eliminarLibro(Libro libro) {
        String sql = "DELETE FROM libros WHERE titulo = ? AND autor = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, libro.getTitle());

            // Si tienes el método getNombreAutores en Libro, úsalo. Si no, usa esto:
            String autor = libro.getAuthors() != null && !libro.getAuthors().isEmpty()
                    ? libro.getAuthors().get(0).getName()
                    : "Desconocido";
            stmt.setString(2, autor);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar el libro: " + e.getMessage());
            return false;
        }
    }

    public List<Libro> listarLibros() {
        return obtenerTodos();
    }

}

