package com.alura.literalura.principal;

import com.alura.literalura.model.*;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner lectura = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();

    private LibroRepository libroRepositorio;
    private AutorRepository autorRepositorio;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepositorio = libroRepository;
        this.autorRepositorio = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ***************************************************
                    1 - Buscar libro por título (Web)
                    2 - Listar libros registrados (BD)
                    3 - Listar autores registrados (BD)
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    6 - Ver cantidad de libros por idioma (Estadísticas)
                    0 - Salir
                    ***************************************************
                    """;
            System.out.println(menu);
            try {
                opcion = lectura.nextInt();
                lectura.nextLine();

                switch (opcion) {
                    case 1:
                        buscarLibroWeb();
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosEnAnio();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 6:
                        mostrarEstadisticasPorIdioma();
                        break;
                    case 0:
                        System.out.println("Cerrando la aplicación...");
                        break;
                    default:
                        System.out.println("Opción inválida");
                }
            } catch (Exception e) {
                System.out.println("Error: Ingrese un valor numérico válido.");
                lectura.nextLine();
            }
        }
    }

    private Datos getDatosLibro() {
        System.out.println("Escribe el nombre del libro que deseas buscar:");
        var nombreLibro = lectura.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        return conversor.obtenerDatos(json, Datos.class);
    }

    private void buscarLibroWeb() {
        Datos datos = getDatosLibro();
        if (datos.resultados() != null && !datos.resultados().isEmpty()) {
            DatosLibro datosLibro = datos.resultados().get(0);
            DatosAutor datosAutor = datosLibro.autor().get(0);

            Autor autor = autorRepositorio.findByNombreIgnoreCase(datosAutor.nombre())
                    .orElseGet(() -> autorRepositorio.save(new Autor(datosAutor)));

            Optional<Libro> libroExistente = libroRepositorio.findByTituloIgnoreCase(datosLibro.titulo());

            if (libroExistente.isEmpty()) {
                Libro libro = new Libro(datosLibro, autor);
                libroRepositorio.save(libro);
                System.out.println("\n--- Libro Guardado con Éxito ---");
                System.out.println(libro);
            } else {
                System.out.println("\n[!] El libro '" + datosLibro.titulo() + "' ya está en la base de datos.");
            }
        } else {
            System.out.println("\n[!] Libro no encontrado en la API.");
        }
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepositorio.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            libros.stream()
                    .sorted(Comparator.comparing(Libro::getTitulo))
                    .forEach(System.out::println);
        }
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepositorio.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            autores.stream()
                    .sorted(Comparator.comparing(Autor::getNombre))
                    .forEach(System.out::println);
        }
    }

    private void listarAutoresVivosEnAnio() {
        System.out.println("Escriba el año que desea consultar:");
        if (lectura.hasNextInt()) {
            var anio = lectura.nextInt();
            lectura.nextLine();
            List<Autor> autoresVivos = autorRepositorio.buscarAutoresVivosEnAnio(anio);

            if (autoresVivos.isEmpty()) {
                System.out.println("No se encontraron autores vivos en el año " + anio);
            } else {
                System.out.println("\n--- AUTORES VIVOS EN " + anio + " ---");
                autoresVivos.forEach(System.out::println);
            }
        } else {
            System.out.println("Año inválido.");
            lectura.nextLine();
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
            Ingrese el idioma para buscar:
            es - español
            en - inglés
            fr - francés
            pt - portugués
            """);
        var idioma = lectura.nextLine();
        List<Libro> libros = libroRepositorio.findByIdioma(idioma);

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en ese idioma.");
        } else {
            libros.forEach(System.out::println);
        }
    }

    private void mostrarEstadisticasPorIdioma() {
        System.out.println("""
            Seleccione el idioma para ver las estadísticas (conteo):
            es - español
            en - inglés
            fr - francés
            pt - portugués
            """);
        var idioma = lectura.nextLine();

        Long cantidad = libroRepositorio.countByIdioma(idioma);

        String nombreIdioma = switch (idioma.toLowerCase()) {
            case "es" -> "español";
            case "en" -> "inglés";
            case "fr" -> "francés";
            case "pt" -> "portugués";
            default -> "el idioma seleccionado";
        };

        System.out.println("\n-------------------------------------------------");
        System.out.println("Cantidad de libros en " + nombreIdioma + ": " + cantidad);
        System.out.println("-------------------------------------------------\n");
    }
}