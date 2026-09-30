package com.supplychain.procurement_service.config;

import com.supplychain.procurement_service.dto.SupplierResponse;
import com.supplychain.procurement_service.exception.SupplierNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SupplierClient {

    private final WebClient webClient;

    private static final String SUPPLIER_SERVICE_URL =
            "http://localhost:8085";

    public SupplierResponse getSupplierById(UUID supplierId) {

        return webClient
                .get()
                .uri(
                        SUPPLIER_SERVICE_URL +
                                "/api/v1/suppliers/{supplierId}",
                        supplierId
                )
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> response
                                .bodyToMono(String.class)
                                .map(body ->
                                        new SupplierNotFoundException(
                                                supplierId
                                        )
                                )
                )
                .bodyToMono(SupplierResponse.class)
                .block();
    }
}