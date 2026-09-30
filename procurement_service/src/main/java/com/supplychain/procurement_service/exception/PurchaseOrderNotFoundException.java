package com.supplychain.procurement_service.exception;

import java.util.UUID;

public class PurchaseOrderNotFoundException extends RuntimeException {

    public PurchaseOrderNotFoundException(UUID id) {
        super("Purchase order not found with id: " + id);
    }
}