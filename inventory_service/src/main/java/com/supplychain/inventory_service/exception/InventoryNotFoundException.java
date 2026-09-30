package com.supplychain.inventory_service.exception;

import java.util.UUID;

public class InventoryNotFoundException
        extends RuntimeException {

    public InventoryNotFoundException(UUID inventoryId) {

        super(
                "Inventory not found with id: "
                        + inventoryId
        );
    }
}