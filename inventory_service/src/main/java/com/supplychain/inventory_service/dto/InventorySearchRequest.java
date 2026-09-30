package com.supplychain.inventory_service.dto;

import com.supplychain.inventory_service.entity.InventoryStatus;

import java.util.UUID;

public record InventorySearchRequest(
        UUID productId,
        UUID warehouseId,
        InventoryStatus status,
        Integer maxAvailableQuantity,
        Integer maxTotalQuantity
) {
}