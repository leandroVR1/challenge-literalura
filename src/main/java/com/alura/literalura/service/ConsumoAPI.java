package com.alura.literalura.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoAPI {
    public String obtenerDatos(String url) {
        // 1. Construyendo el Cliente
        HttpClient client = HttpClient.newHttpClient();

        // 2. Construyendo la Solicitud (Request)
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        // 3. Gestionando la Respuesta (Response)
        HttpResponse<String> response = null;
        try {
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            // Manejo de errores para que la app no explote si falla el internet
            throw new RuntimeException("Error en la conexión con la API: " + e.getMessage());
        }

        // Retornamos el cuerpo (JSON)
        return response.body();
    }
}
