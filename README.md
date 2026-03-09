# 📚 Literalura - Challenge Alura Latam

![Java](https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-brightgreen?style=for-the-badge&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18.3-blue?style=for-the-badge&logo=postgresql)

## 📖 Descripción del Proyecto
Literalura es un catálogo literario que permite la gestión de libros y autores consumiendo datos de la API externa **Gutendex**. El proyecto se enfoca en la persistencia de datos relacionales, el manejo de estadísticas y la interacción por consola, aplicando los conceptos de **Spring Data JPA**.



---

## 🚀 Funcionalidades Principales
La aplicación ofrece un menú interactivo con las siguientes opciones:

1.  **Búsqueda por Título (Web)**: Consulta la API y guarda automáticamente el libro y su autor si no existen en la base de datos.
2.  **Catálogo de Libros**: Lista todos los libros almacenados, mostrando título, autor, idioma y descargas.
3.  **Registro de Autores**: Muestra la lista de autores con sus fechas de nacimiento y fallecimiento.
4.  **Filtro de Autores Vivos**: Permite buscar qué autores de la base de datos estaban vivos en un año específico.
5.  **Búsqueda por Idioma**: Filtra los libros registrados por su código de idioma (es, en, fr, pt).
6.  **Estadísticas**: Proporciona el conteo de libros por idioma utilizando *Derived Queries*.



---

## 🛠️ Tecnologías y Herramientas
* **Java SDK 25**: Uso de las últimas características del lenguaje.
* **Spring Boot**: Framework principal para la inversión de control y configuración.
* **Spring Data JPA**: Para el manejo de repositorios y consultas JPQL.
* **PostgreSQL**: Motor de base de datos para la persistencia.
* **Maven**: Gestión de dependencias y ciclo de vida del proyecto.
* **Jackson/Gson**: Para el mapeo de datos JSON provenientes de la API.

---

## ⚙️ Configuración e Instalación

### Requisitos
* JDK 25 instalado.
* PostgreSQL corriendo localmente.

### Base de Datos
Crea una base de datos llamada `literalura` en tu PostgreSQL y configura el archivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false