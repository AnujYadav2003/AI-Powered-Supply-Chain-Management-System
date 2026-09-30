package com.supplychain.inventory_service.client;

import com.supplychain.inventory_service.dto.WarehouseResponse;
import com.supplychain.inventory_service.exception.WarehouseNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WarehouseClient {

    private final WebClient webClient;

    private static final String WAREHOUSE_SERVICE_URL =
            "http://localhost:8084";

    public WarehouseResponse getWarehouseById(
            UUID warehouseId
    ) {

        return webClient
                .get()
                .uri(
                        WAREHOUSE_SERVICE_URL
                                + "/api/v1/warehouses/{id}",
                        warehouseId
                )
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> response
                                .bodyToMono(String.class)
                                .map(body ->
                                        new WarehouseNotFoundException(
                                                warehouseId
                                        )
                                )
                )
                .bodyToMono(WarehouseResponse.class)
                .block();
    }
}