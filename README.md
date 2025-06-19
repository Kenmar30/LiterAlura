# LiterAlura — Catálogo literario en consola 🌟

**LiterAlura** es una aplicación de consola desarrollada en Java que permite buscar libros desde la API de Gutendex, guardarlos en una base de datos SQLite y realizar diversas operaciones como filtrado por idioma, autor, estadísticas y más.

---

## ✨ Características principales

* 🔍 Buscar libros por título desde [Gutendex API](https://gutendex.com/)
* 💾 Guardar libros localmente con:

  * Título
  * Autor (con año de nacimiento y muerte)
  * Idioma
  * Fecha en que se guardó
* 📋 Listar libros guardados
* 👤 Buscar libros por autor
* 🌎 Ver libros por idioma
* 📊 Estadísticas:

  * Total de libros
  * Cantidad de autores distintos
  * Libros por idioma
* \u{1f5d1️} Eliminar libros
* 📜 Listar autores vivos en un año determinado

---

## ⛏ Estructura del proyecto

```
LiterAlura/
├── literAlura.model/           # Clases de dominio (Libro, Autor)
├── literAlura.repository/      # Lógica de acceso a datos (LibroRepository)
├── literAlura.ui/               # Interfaz de consola (ConsolaInteractiva)
└── Main.java                  # Punto de entrada
```

---

## ⚙️ Tecnologías utilizadas

* Java 21
* SQLite (vía JDBC)
* Gson para parseo de JSON
* API REST pública: Gutendex

---

## ⚡ Requisitos para ejecutar

* Java 21 o superior
* IntelliJ IDEA, Eclipse o cualquier IDE Java compatible

---

## 📄 Uso

1. Clona el repositorio
2. Ejecuta `Main.java`
3. Interactúa con el menú:

```
📚 Bienvenido a LiterAlura
1. Buscar libros por título
2. Listar libros guardados
3. Buscar por autor
4. Ver libros por idioma
5. Ver estadísticas
6. Eliminar libro guardado
7. 📜 Listar autores vivos en un año determinado
8. Salir
```

---

## 🚩 Notas

* Si experimentas errores como `SQLITE_BUSY`, asegúrate de que la base de datos no esté siendo usada por otro proceso.
* La tabla `libros` se ajusta automáticamente para agregar las columnas `birth_year` y `death_year` si no existen.
* La fecha guardada se maneja con `LocalDate` y se convierte internamente a `LocalDateTime`.

---

## 🚀 Futuras mejoras

* Interfaz gráfica (GUI)
* Exportar libros guardados a CSV o PDF
* Filtros más avanzados
* Paginar resultados de la API

---

## 💖 Autor

Desarrollado con paciencia, café y tenacidad por **Kenmar30**.

---

## ✨ Licencia

Este proyecto es de uso personal y educativo. 



