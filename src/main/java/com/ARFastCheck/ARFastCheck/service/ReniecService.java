package com.ARFastCheck.ARFastCheck.service;

import com.ARFastCheck.ARFastCheck.dto.ReniecResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
public class ReniecService {

    @Value("${reniec.api.url}")
    private String apiUrl;

    @Value("${reniec.api.token}")
    private String apiToken;

    private final RestTemplate rest;

    public ReniecService(RestTemplate rest) {
        this.rest = rest;
    }

    public Optional<ReniecResponse> consultarDni(String dni) {
        if (dni == null || !dni.matches("\\d{8}")) {
            System.out.println("[RENIEC] DNI inválido localmente: " + dni);
            return Optional.empty();
        }

        String url = UriComponentsBuilder
                .fromHttpUrl(apiUrl)
                .queryParam("numero", dni)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ReniecResponse> resp =
                    rest.exchange(url, HttpMethod.GET, entity, ReniecResponse.class);

            System.out.println("[RENIEC] URL: " + url);
            System.out.println("[RENIEC] Status: " + resp.getStatusCode());

            if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
                ReniecResponse r = resp.getBody();

                // 🔎 Log detallado de atributos
                System.out.println("[RENIEC] Datos mapeados ->");
                System.out.println("   DNI: " + r.getDocumentNumber());
                System.out.println("   Nombre completo: " + r.getFullName());
                System.out.println("   Nombres: " + r.getFirstName());
                System.out.println("   Apellidos: " + r.getFirstLastName() + " " + r.getSecondLastName());

                return Optional.of(r);
            }

            return Optional.empty();

        } catch (HttpStatusCodeException e) {
            // Muestra detalle del error
            System.out.println("[RENIEC] Error status: " + e.getStatusCode());
            System.out.println("[RENIEC] Respuesta: " + e.getResponseBodyAsString());
            return Optional.empty();
        } catch (ResourceAccessException e) {
            System.out.println("[RENIEC] Error de conexión o timeout: " + e.getMessage());
            return Optional.empty();
        }
    }

    @PostConstruct
    public void verificarToken() {
        if (apiToken == null || apiToken.isBlank()) {
            System.out.println("[RENIEC] Token vacío o no configurado.");
        } else {
            System.out.println("[RENIEC] Token cargado (longitud: " + apiToken.length() + ")");
        }
    }
}
