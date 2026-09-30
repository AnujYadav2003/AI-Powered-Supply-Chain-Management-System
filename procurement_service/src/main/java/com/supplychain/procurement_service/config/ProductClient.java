package com.supplychain.procurement_service.config;

import com.supplychain.procurement_service.dto.ProductResponse;
import com.supplychain.procurement_service.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductClient {

    private final WebClient webClient;

    private static final String PRODUCT_SERVICE_URL =
            "http://localhost:8081";

    public ProductResponse getProductById(UUID productId) {

        return webClient
                .get()
                .uri(
                        PRODUCT_SERVICE_URL +
                                "/api/v1/products/{productId}",
                        productId
                )
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> response
                                .bodyToMono(String.class)
                                .map(body ->
                                        new ProductNotFoundException(
                                                productId
                                        )
                                )
                )
                .bodyToMono(ProductResponse.class)
                .block();
    }
}