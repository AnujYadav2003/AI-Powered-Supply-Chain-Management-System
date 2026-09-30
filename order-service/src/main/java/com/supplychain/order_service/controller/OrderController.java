package com.supplychain.order_service.controller;

import com.supplychain.order_service.dto.OrderRequest;
import com.supplychain.order_service.dto.OrderResponse;
import com.supplychain.order_service.entity.OrderStatus;
import com.supplychain.order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request
    ) {

        OrderResponse response =
                orderService.createOrder(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable UUID orderId
    ) {

        return ResponseEntity.ok(
                orderService.getOrderById(orderId)
        );
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<OrderResponse>>
    getOrdersByCustomer(
            @PathVariable UUID customerId,
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                orderService.getOrdersByCustomer(
                        customerId,
                        pageable
                )
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<OrderResponse>>
    getOrdersByStatus(
            @PathVariable OrderStatus status,
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                orderService.getOrdersByStatus(
                        status,
                        pageable
                )
        );
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse>
    updateOrderStatus(
            @PathVariable UUID orderId,
            @RequestParam OrderStatus status
    ) {

        return ResponseEntity.ok(
                orderService.updateOrderStatus(
                        orderId,
                        status
                )
        );
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable UUID orderId
    ) {

        orderService.cancelOrder(orderId);

        return ResponseEntity.noContent().build();
    }
}