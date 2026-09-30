package com.supplychain.order_service.service;

import com.supplychain.order_service.dto.OrderRequest;
import com.supplychain.order_service.dto.OrderResponse;
import com.supplychain.order_service.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);

    OrderResponse getOrderById(UUID orderId);

    Page<OrderResponse> getOrdersByCustomer(
            UUID customerId,
            Pageable pageable
    );

    Page<OrderResponse> getOrdersByStatus(
            OrderStatus status,
            Pageable pageable
    );

    OrderResponse updateOrderStatus(
            UUID orderId,
            OrderStatus status
    );

    void cancelOrder(UUID orderId);
}