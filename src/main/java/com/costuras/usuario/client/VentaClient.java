package com.costuras.usuario.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.costuras.usuario.dto.HistorialVentaResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;




@Component
@RequiredArgsConstructor
public class VentaClient {

@Value("${venta.url}")
    private String ventaUrl;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public List<HistorialVentaResponse> getHistorialByToken(String bearerToken) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ventaUrl + "/ventas/mis-ventas"))
                .header("Authorization", bearerToken)
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), new TypeReference<List<HistorialVentaResponse>>() {});
            }
            return List.of();
        } catch (Exception e) {
            throw new RuntimeException("error al consultar por ventas: " + e.getMessage(), e);
        }
    }
}
