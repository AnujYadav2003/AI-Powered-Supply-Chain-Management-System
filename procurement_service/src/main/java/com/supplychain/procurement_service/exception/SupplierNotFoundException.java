package com.supplychain.procurement_service.exception;

import java.util.UUID;

public class SupplierNotFoundException extends RuntimeException {

    public SupplierNotFoundException(UUID supplierId) {
        super("Supplier not found with id: " + supplierId);
    }
}