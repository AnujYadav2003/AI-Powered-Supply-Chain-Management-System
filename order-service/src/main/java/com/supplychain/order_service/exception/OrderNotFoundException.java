package com.supplychain.order_service.exception;

import java.util.UUID;

public class OrderNotFoundException
        extends RuntimeException {

    public OrderNotFoundException(UUID orderId) {
        super("Order not found with id: " + orderId);
    }
}