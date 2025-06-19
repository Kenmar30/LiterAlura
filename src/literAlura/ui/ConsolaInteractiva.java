package literAlura.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import literAlura.LocalDateTimeAdapter;
import literAlura.model.Autor;
import literAlura.model.Libro;
import literAlura.model.ResultadoBusqueda;
import literAlura.repository.LibroRepository;
import literAlura.service.APIClient;
import literAlura.LocalDateAdapter;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;


public class ConsolaInteractiva {
    private final Scanner scanner = new Scanner(System.in);
    private final APIClient apiClient = new APIClient();
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    private final List<Libro> librosGuardados = new ArrayList<>();
    private final LibroRepository libroRepo = new LibroRepository();

    public void iniciar() {
        int opcion = 0;

        do {
            System.out.println("\n📚 Bienvenido a LiterAlura");
            System.out.println("1. Buscar libros por título");
            System.out.println("2. Listar libros guardados");
            System.out.println("3. Buscar por autor");
            System.out.println("4. Ver libros por idioma");
            System.out.println("5. Ver estadísticas");
            System.out.println("6. Eliminar libro guardado");
            System.out.println("7. 📜 Listar autores vivos en un año determinado");
            System.out.println("8. Salir");

            System.out.print("📌 Elige una opción del 1 al 8: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Entrada inválida. Intenta de nuevo.");
                continue;
            }

            switch (opcion) {
                case 1 -> buscarPorTitulo();
                case 2 -> listarGuardados();
                case 3 -> buscarPorAutor();
                case 4 -> verLibrosPorIdioma();
                case 5 -> mostrarEstadisticas();
                case 6 -> eliminarLibro();
                case 7 -> {
                    listarAutoresVivos();
                    break;
                }
                case 8 ->
                        System.out.println("👋 ¡Gracias por explorar con LiterAlura! Vuelve pronto por más lecturas inspiradoras.");
                default -> System.out.println("❌ Opción inválida. Intenta de nuevo.");
            }

        } while (opcion != 8);

    }

    private void buscarPorTitulo() {
        System.out.print("🔎 Escribe el título a buscar: ");
        String titulo = scanner.nextLine();
        String encoded = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
        String url = "https://gutendex.com/books/?search=" + encoded;
        System.out.println("[DEBUG] URL → " + url);  // 🕵️‍♂️
        String json = apiClient.get(url);

        if (json == null || json.isEmpty()) {
            System.out.println("⚠️ No se pudo obtener respuesta de la API.");
            return;
        }

        ResultadoBusqueda resultado = gson.fromJson(json, ResultadoBusqueda.class);
        if (resultado == null || resultado.getResults() == null) {
            System.out.println("⚠️ La respuesta de la API no es válida.");
            return;
        }

        List<Libro> resultados = resultado.getResults();

        if (resultados.isEmpty()) {
            System.out.println("😕 No se encontraron libros.");
            System.out.println("💡 Consejo: intenta con una palabra clave más simple, como 'pride' en lugar de 'Pride and Prejudice'.");
            System.out.println("   También puedes buscar por el apellido del autor, por ejemplo: 'Austen'.");
        } else {
            for (int i = 0; i < resultados.size(); i++) {
                System.out.println("\n📖 Libro " + (i + 1) + ":\n" + resultados.get(i));
            }

            System.out.print("💾 ¿Deseas guardar alguno? Escribe los números separados por comas (0 para ninguno): ");

            String input = scanner.nextLine();

            if (!input.trim().equals("0")) {
                String[] nums = input.split(",");
                for (String numStr : nums) {
                    try {
                        int num = Integer.parseInt(numStr.trim());
                        if (num > 0 && num <= resultados.size()) {
                            Libro libro = resultados.get(num - 1);
                            // Verifica si ya está guardado antes de agregarlo
                            if (!libroRepo.existeLibro(libro.getTitle(), String.valueOf(libro.getAuthors().get(0)))) {
                                librosGuardados.add(libro);
                                libroRepo.guardarLibro(libro);
                                System.out.println("✅ Libro " + num + " guardado.");
                            } else {
                                System.out.println("⚠️ El libro \"" + libro.getTitle() + "\" ya está guardado.");
                            }
                        } else {
                            System.out.println("❌ Número inválido: " + num);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Entrada no válida: " + numStr);
                    }
                }

            }
        }
    }

    private void listarGuardados() {
        List<Libro> libros = libroRepo.obtenerTodos();

        if (libros.isEmpty()) {
            System.out.println("📂 No tienes libros guardados aún.");
            return;
        }

        System.out.println("\n📚 Libros guardados:");
        for (int i = 0; i < libros.size(); i++) {
            Libro libro = libros.get(i);
            String autor = libro.getAuthors() != null && !libro.getAuthors().isEmpty()
                    ? libro.getAuthors().get(0).getName()
                    : "Desconocido";

            String idioma = libro.getLanguages() != null && !libro.getLanguages().isEmpty()
                    ? libro.getLanguages().get(0)
                    : "desconocido";

            System.out.println("🔸 " + (i + 1) + ". \"" + libro.getTitle() + "\" de " + autor +
                    " | Idioma: " + idioma +
                    (libro.getFechaGuardada() != null ? " | 📅 Guardado el: " + libro.getFechaGuardada() : ""));
        }
    }


    private void buscarPorAutor() {
        System.out.print("👤 Nombre del autor a buscar: ");
        String autor = scanner.nextLine().trim();
        String url = "https://gutendex.com/books/?search=" + URLEncoder.encode(autor, StandardCharsets.UTF_8);
        String json = apiClient.get(url);

        if (json == null || json.isEmpty()) {
            System.out.println("⚠️ No se pudo obtener respuesta de la API.");
            return;
        }

        ResultadoBusqueda resultado = gson.fromJson(json, ResultadoBusqueda.class);
        List<Libro> resultados = resultado.getResults();

        if (resultados.isEmpty()) {
            System.out.println("😕 No se encontraron libros para ese autor.");
        } else {
            resultados.forEach(libro -> {
                System.out.println(libro);
                System.out.println("-".repeat(40));
            });
        }
    }

    private void verLibrosPorIdioma() {
        System.out.print("🌐 Ingresa el código del idioma (por ejemplo, en, es, fr): ");
        String idioma = scanner.nextLine().trim();

        if (idioma.isEmpty()) {
            System.out.println("⚠️ Debes escribir un código de idioma. Ejemplos: en (inglés), es (español), fr (francés).");

            return;
        }

        String url = "https://gutendex.com/books/?languages=" + URLEncoder.encode(idioma, StandardCharsets.UTF_8);
        String json = apiClient.get(url);

        if (json == null || json.isEmpty()) {
            System.out.println("⚠️ No se pudo obtener respuesta de la API.");
            return;
        }

        ResultadoBusqueda resultado = gson.fromJson(json, ResultadoBusqueda.class);
        List<Libro> resultados = resultado.getResults();

        if (resultados.isEmpty()) {
            System.out.println("😕 No se encontraron libros en el idioma: " + idioma);
        } else {
            for (Libro libro : resultados) {
                System.out.println(libro);
                System.out.println("=".repeat(40));
            }


        }


    }

    private void mostrarEstadisticas() {
        int total = libroRepo.contarTotalLibros();
        int autores = libroRepo.contarAutoresDistintos();
        Map<String, Integer> porIdioma = libroRepo.contarLibrosPorIdioma();

        System.out.println("\n📊 Estadísticas:");
        System.out.println("📚 Total de libros guardados: " + total);
        System.out.println("👤 Total de autores distintos: " + autores);
        System.out.println("🌐 Libros por idioma:");
        porIdioma.forEach((idioma, cantidad) ->
                System.out.println("   - " + idioma + ": " + cantidad));
    }

    private void eliminarLibro() {
        List<Libro> libros = libroRepo.listarLibros();

        if (libros.isEmpty()) {
            System.out.println("📂 No tienes libros guardados aún.");
            return;
        }

        System.out.println("\n📚 Libros guardados:");
        for (int i = 0; i < libros.size(); i++) {
            Libro libro = libros.get(i);
            System.out.println((i + 1) + ". \"" + libro.getTitle() + "\" de " + libro.getAuthors());
        }

        System.out.print("🗑️ Ingresa el número del libro que quieres eliminar: ");
        try {
            int opcion = Integer.parseInt(scanner.nextLine());

            if (opcion < 1 || opcion > libros.size()) {
                System.out.println("⚠️ Opción inválida.");
                return;
            }

            Libro libroAEliminar = libros.get(opcion - 1);
            boolean eliminado = libroRepo.eliminarLibro(libroAEliminar);

            if (eliminado) {
                System.out.println("✅ Libro eliminado con éxito.");
            } else {
                System.out.println("❌ No se pudo eliminar el libro.");
            }
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Debes ingresar un número válido.");
        }
    }

    private void listarAutoresVivos() {
        System.out.print("Ingrese un año (ej. 1900): ");
        int anio = scanner.nextInt();
        scanner.nextLine(); // limpiar buffer

        List<Libro> libros = libroRepo.listarLibros();

        Set<String> autoresVivos = libros.stream()
                .flatMap(libro -> libro.getAuthors().stream())
                .filter(autor -> autor.getBirthYear() != null && autor.getBirthYear() <= anio)
                .filter(autor -> autor.getDeathYear() == null || autor.getDeathYear() >= anio)
                .map(Autor::toString)
                .collect(Collectors.toCollection(TreeSet::new)); // evita duplicados y ordena

        if (autoresVivos.isEmpty()) {
            System.out.println("😢 No se encontraron autores vivos en el año " + anio);
        } else {
            System.out.println("\n📜 Autores vivos en el año " + anio + ":");
            autoresVivos.forEach(nombre -> System.out.println(" - " + nombre));
        }
    }

}




