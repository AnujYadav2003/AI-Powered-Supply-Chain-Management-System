package com.supplychain.inventory_service.mapper;

import com.supplychain.inventory_service.dto.InventoryResponse;
import com.supplychain.inventory_service.entity.Inventory;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface InventoryMapper {

    @Mapping(
            target = "availableQuantity",
            expression =
                    "java(inventory.getTotalQuantity() - inventory.getReservedQuantity())"
    )
    InventoryResponse toResponse(Inventory inventory);
}