package com.supplychain.warehouse_service.exception;

import java.util.UUID;

public class WarehouseNotFoundException extends RuntimeException {

    public WarehouseNotFoundException(UUID id) {
        super("Warehouse not found with id: " + id);
    }
}