package com.supplychain.order_service.serviceImpl;

import com.supplychain.order_service.config.ProductClient;
import com.supplychain.order_service.dto.OrderItemRequest;
import com.supplychain.order_service.dto.OrderRequest;
import com.supplychain.order_service.dto.OrderResponse;
import com.supplychain.order_service.dto.ProductResponse;
import com.supplychain.order_service.entity.Order;
import com.supplychain.order_service.entity.OrderItem;
import com.supplychain.order_service.entity.OrderStatus;
import com.supplychain.order_service.exception.OrderNotFoundException;
import com.supplychain.order_service.exception.OrderStatusTransitionException;
import com.supplychain.order_service.mapper.OrderMapper;
import com.supplychain.order_service.repository.OrderRepository;
import com.supplychain.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductClient productClient;


    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        Order order = Order.builder()
                .customerId(request.getCustomerId())
                .shippingAddress(request.getShippingAddress())
                .status(OrderStatus.CREATED)
                .totalAmount(BigDecimal.ZERO)
                .items(new ArrayList<>())
                .build();

        BigDecimal orderTotal = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {

            ProductResponse product =
                    productClient.getProductById(
                            itemRequest.getProductId()
                    );

            if (!"ACTIVE".equalsIgnoreCase(product.getStatus())) {
                throw new IllegalStateException(
                        "Product is not available for ordering: "
                                + itemRequest.getProductId()
                );
            }

            BigDecimal unitPrice = product.getUnitPrice();

            BigDecimal itemTotal =
                    unitPrice.multiply(
                            BigDecimal.valueOf(
                                    itemRequest.getQuantity()
                            )
                    );

            OrderItem orderItem = OrderItem.builder()
                    .productId(itemRequest.getProductId())
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(unitPrice)
                    .totalPrice(itemTotal)
                    .order(order)
                    .build();

            order.getItems().add(orderItem);

            orderTotal = orderTotal.add(itemTotal);
        }

        order.setTotalAmount(orderTotal);

        Order savedOrder = orderRepository.save(order);

        return orderMapper.toResponse(savedOrder);
    }


    @Override
    public OrderResponse getOrderById(UUID orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(
                        () -> new OrderNotFoundException(orderId)
                );

        return orderMapper.toResponse(order);
    }


    @Override
    public Page<OrderResponse> getOrdersByCustomer(
            UUID customerId,
            Pageable pageable
    ) {

        return orderRepository
                .findByCustomerId(customerId, pageable)
                .map(orderMapper::toResponse);
    }


    @Override
    public Page<OrderResponse> getOrdersByStatus(
            OrderStatus status,
            Pageable pageable
    ) {

        return orderRepository
                .findByStatus(status, pageable)
                .map(orderMapper::toResponse);
    }


    @Override
    @Transactional
    public OrderResponse updateOrderStatus(
            UUID orderId,
            OrderStatus newStatus
    ) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(
                        () -> new OrderNotFoundException(orderId)
                );

        OrderStatus currentStatus = order.getStatus();

        validateStatusTransition(
                currentStatus,
                newStatus
        );

        order.setStatus(newStatus);

        return orderMapper.toResponse(order);
    }


    @Override
    @Transactional
    public void cancelOrder(UUID orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(
                        () -> new OrderNotFoundException(orderId)
                );

        OrderStatus currentStatus = order.getStatus();

        if (currentStatus == OrderStatus.CANCELLED) {
            throw new OrderStatusTransitionException(
                    currentStatus,
                    OrderStatus.CANCELLED
            );
        }

        if (currentStatus == OrderStatus.SHIPPED
                || currentStatus == OrderStatus.DELIVERED) {

            throw new OrderStatusTransitionException(
                    currentStatus,
                    OrderStatus.CANCELLED
            );
        }

        order.setStatus(OrderStatus.CANCELLED);
    }


    private void validateStatusTransition(
            OrderStatus currentStatus,
            OrderStatus newStatus
    ) {

        if (currentStatus == newStatus) {
            throw new OrderStatusTransitionException(
                    currentStatus,
                    newStatus
            );
        }

        boolean validTransition = switch (currentStatus) {

            case CREATED ->
                    newStatus == OrderStatus.CONFIRMED
                            || newStatus == OrderStatus.CANCELLED;

            case CONFIRMED ->
                    newStatus == OrderStatus.PROCESSING
                            || newStatus == OrderStatus.CANCELLED;

            case PROCESSING ->
                    newStatus == OrderStatus.SHIPPED
                            || newStatus == OrderStatus.CANCELLED;

            case SHIPPED ->
                    newStatus == OrderStatus.DELIVERED;

            case DELIVERED,
                 CANCELLED ->
                    false;
        };

        if (!validTransition) {
            throw new OrderStatusTransitionException(
                    currentStatus,
                    newStatus
            );
        }
    }
}