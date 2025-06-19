package literAlura.repository;

import literAlura.model.Autor;
import literAlura.model.Libro;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class LibroRepository {

    private static final String URL = "jdbc:sqlite:literAlura.db";

    public LibroRepository() {
        asegurarColumnasExtendidas();
    }

    public void guardarLibro(Libro libro) {
        String sql = "INSERT INTO libros (titulo, autor, idioma, fecha_guardada, birth_year, death_year) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String titulo = libro.getTitle();

            Autor autorObj = libro.getAuthors() != null && !libro.getAuthors().isEmpty()
                    ? libro.getAuthors().get(0)
                    : new Autor();

            String autor = autorObj.getName() != null ? autorObj.getName() : "Desconocido";
            Integer birthYear = autorObj.getBirthYear();
            Integer deathYear = autorObj.getDeathYear();

            String idioma = libro.getLanguages() != null && !libro.getLanguages().isEmpty()
                    ? libro.getLanguages().get(0)
                    : "desconocido";

            LocalDate fechaActual = LocalDate.now();

            pstmt.setString(1, titulo);
            pstmt.setString(2, autor);
            pstmt.setString(3, idioma);
            pstmt.setString(4, fechaActual.toString());

            if (birthYear != null) pstmt.setInt(5, birthYear); else pstmt.setNull(5, Types.INTEGER);
            if (deathYear != null) pstmt.setInt(6, deathYear); else pstmt.setNull(6, Types.INTEGER);

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
        String sql = "SELECT titulo, autor, idioma, fecha_guardada, birth_year, death_year FROM libros";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String titulo = rs.getString("titulo");
                String idioma = rs.getString("idioma");
                String fechaGuardada = rs.getString("fecha_guardada");

                int birthYear = rs.getInt("birth_year");
                int deathYear = rs.getInt("death_year");

                Autor autor = new Autor();
                autor.setName(rs.getString("autor"));
                autor.setBirthYear(rs.wasNull() ? null : birthYear);
                autor.setDeathYear(rs.wasNull() ? null : deathYear);

                List<Autor> autores = List.of(autor);
                List<String> idiomas = List.of(idioma);

                LocalDate fecha = null;
                if (fechaGuardada != null && !fechaGuardada.isBlank()) {
                    fecha = LocalDate.parse(fechaGuardada);
                }

                Libro libro = new Libro(titulo, autores, idiomas, fecha);
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
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT titulo, autor, idioma, fecha_guardada, birth_year, death_year FROM libros";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String titulo = rs.getString("titulo");
                String idioma = rs.getString("idioma");
                String fechaStr = rs.getString("fecha_guardada");

                int birthYear = rs.getInt("birth_year");
                int deathYear = rs.getInt("death_year");

                Autor autor = new Autor();
                autor.setName(rs.getString("autor"));
                autor.setBirthYear(rs.wasNull() ? null : birthYear);
                autor.setDeathYear(rs.wasNull() ? null : deathYear);

                List<Autor> autores = List.of(autor);
                List<String> idiomas = List.of(idioma);

                LocalDate fechaGuardada = null;
                if (fechaStr != null && !fechaStr.isBlank()) {
                    fechaGuardada = LocalDate.parse(fechaStr);
                }

                Libro libro = new Libro(titulo, autores, idiomas, fechaGuardada);
                libros.add(libro);
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Error al leer libros: " + e.getMessage());
        }

        return libros;
    }

    private void asegurarColumnasExtendidas() {
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {

            try {
                stmt.execute("ALTER TABLE libros ADD COLUMN birth_year INTEGER");
            } catch (SQLException e) {
                if (!e.getMessage().contains("duplicate column name")) {
                    System.out.println("⚠️ Error al agregar columna birth_year: " + e.getMessage());
                }
            }

            try {
                stmt.execute("ALTER TABLE libros ADD COLUMN death_year INTEGER");
            } catch (SQLException e) {
                if (!e.getMessage().contains("duplicate column name")) {
                    System.out.println("⚠️ Error al agregar columna death_year: " + e.getMessage());
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al asegurar columnas extendidas: " + e.getMessage());
        }
    }
}




