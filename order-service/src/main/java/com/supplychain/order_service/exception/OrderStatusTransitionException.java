package com.supplychain.order_service.exception;

import com.supplychain.order_service.entity.OrderStatus;

public class OrderStatusTransitionException extends RuntimeException {

    public OrderStatusTransitionException(
            OrderStatus currentStatus,
            OrderStatus requestedStatus
    ) {
        super(
                "Invalid order status transition from "
                        + currentStatus
                        + " to "
                        + requestedStatus
        );
    }
}