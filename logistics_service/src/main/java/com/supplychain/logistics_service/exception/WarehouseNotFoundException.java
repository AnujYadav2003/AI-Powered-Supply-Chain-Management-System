package com.supplychain.logistics_service.exception;

import java.util.UUID;

public class WarehouseNotFoundException extends RuntimeException {

    public WarehouseNotFoundException(UUID warehouseId) {
        super("Warehouse not found with id: " + warehouseId);
    }
}