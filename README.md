# 📚 LiterAlura

Aplicación de consola para explorar, buscar y gestionar libros del Proyecto Gutenberg, usando la API de Gutendex y almacenamiento en SQLite.

---

## 🚀 Funcionalidades

- 🔎 Buscar libros por título o autor
- 💾 Guardar libros localmente
- 🗃️ Listar libros guardados
- 🌐 Filtrar por idioma
- 📊 Ver estadísticas (total de libros, idiomas, autores distintos)
- 🗑️ Eliminar libros guardados
- 📅 Guarda la fecha de cada libro añadido

---

## ⚙️ Tecnologías

- Java 21
- Maven
- Gson (JSON)
- SQLite (base de datos)
- Gutendex API

---

## 📦 Instalación

1. Clona el repositorio:

   ```bash
   git clone https://github.com/Kenmar30/LiterAlura.git
   cd LiterAlura
   
Compila con Maven:
mvn compile

Ejecuta la aplicación:
mvn exec:java -Dexec.mainClass="literAlura.Main"

🗃️ Estructura del Proyecto
LiterAlura/
├── src/
│   └── literAlura/
│       ├── model/
│       ├── repository/
│       ├── service/
│       ├── ui/
│       └── Main.java
├── libros.db (⚠️ ignorado por Git)
├── pom.xml
└── README.md

📖 Inspiración
Este proyecto nace del amor por la literatura libre y el aprendizaje de buenas prácticas en desarrollo Java, con estructura MVC simplificada y persistencia.

🧑‍💻 Autor
Kenmar30
Desarrollador y custodio de bibliotecas digitales en consola.

🌐 Créditos
Gutendex API por facilitar el acceso a libros clásicos.

Inspirado por los desafíos de Alura Latam.

📝 Licencia
Este proyecto es de código abierto y está bajo la licencia MIT.