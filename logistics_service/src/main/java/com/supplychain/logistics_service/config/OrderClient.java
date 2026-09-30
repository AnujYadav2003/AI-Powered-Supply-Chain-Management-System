package com.supplychain.logistics_service.config;

import com.supplychain.logistics_service.dto.OrderResponse;
import com.supplychain.logistics_service.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderClient {

    private final WebClient webClient;

    private static final String ORDER_SERVICE_URL =
            "http://localhost:8082";

    public OrderResponse getOrderById(UUID orderId) {

        return webClient
                .get()
                .uri(
                        ORDER_SERVICE_URL +
                                "/api/v1/orders/{orderId}",
                        orderId
                )
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> response
                                .bodyToMono(String.class)
                                .map(body ->
                                        new OrderNotFoundException(
                                                orderId
                                        )
                                )
                )
                .bodyToMono(OrderResponse.class)
                .block();
    }
}