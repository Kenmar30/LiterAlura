package literAlura.model;

import java.time.LocalDate;
import java.util.List;

public class Libro {
    private String title;
    private List<Autor> authors;
    private List<String> languages;
    private LocalDate fechaGuardada;

    // Constructor sin fecha (para libros que vienen de la API)
    public Libro(String title, List<Autor> authors, List<String> languages) {
        this.title = title;
        this.authors = authors;
        this.languages = languages;
    }

    // Constructor completo (para libros desde la base de datos)
    public Libro(String title, List<Autor> authors, List<String> languages, LocalDate fechaGuardada) {
        this.title = title;
        this.authors = authors;
        this.languages = languages;
        this.fechaGuardada = fechaGuardada;
    }

    public String getTitle() {
        return title;
    }

    public String getNombreAutores() {
        return authors.stream()
                .map(Autor::getName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("Autor desconocido");
    }

    public List<Autor> getAuthors() {
        return authors;
    }

    // Obtener el primer autor
    public Autor getAutor() {
        return authors != null && !authors.isEmpty() ? authors.get(0) : null;
    }

    public List<String> getLanguages() {
        return languages;
    }

    // Devuelve la fecha o una predeterminada para evitar errores
    public LocalDate getFechaGuardada() {
        return fechaGuardada != null ? fechaGuardada : LocalDate.MIN;
    }

    public void setFechaGuardada(LocalDate fechaGuardada) {
        this.fechaGuardada = fechaGuardada;
    }

    @Override
    public String toString() {
        return "📖 Título: " + title +
                "\n👤 Autor(es): " + getNombreAutores() +
                "\n🌐 Idiomas: " + languages +
                (fechaGuardada != null ? "\n🗓️ Guardado en: " + fechaGuardada : "") +
                "\n";
    }
}
