package literAlura.model;

import java.time.LocalDateTime;
import java.util.List;

public class Libro {
    private String title;
    private List<Autor> authors;
    private List<String> languages;
    private LocalDateTime fechaGuardada; // 🕰️ Nuevo campo

    // Constructor sin fecha (para libros que vienen de la API)
    public Libro(String title, List<Autor> authors, List<String> languages) {
        this.title = title;
        this.authors = authors;
        this.languages = languages;
    }

    // Constructor completo (para libros desde la base de datos)
    public Libro(String title, List<Autor> authors, List<String> languages, LocalDateTime fechaGuardada) {
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

    public List<String> getLanguages() {
        return languages;
    }

    public LocalDateTime getFechaGuardada() {
        return fechaGuardada;
    }

    @Override
    public String toString() {
        return "📖 Título: " + title +
                "\n👤 Autor(es): " + authors +
                "\n🌐 Idiomas: " + languages +
                (fechaGuardada != null ? "\n🗓️ Guardado en: " + fechaGuardada.toString() : "") +
                "\n";
    }

    public void setFechaGuardada(String fecha) {
    }


}

