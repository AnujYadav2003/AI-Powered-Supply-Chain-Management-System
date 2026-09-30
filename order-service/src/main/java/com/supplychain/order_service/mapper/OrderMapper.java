package com.supplychain.order_service.mapper;


import com.supplychain.order_service.dto.OrderItemResponse;
import com.supplychain.order_service.dto.OrderResponse;
import com.supplychain.order_service.entity.Order;
import com.supplychain.order_service.entity.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toResponse(Order order);
}