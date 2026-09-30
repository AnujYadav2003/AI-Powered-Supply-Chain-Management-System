package com.supplychain.procurement_service.exception;

public class DuplicatePurchaseOrderException extends RuntimeException {

    public DuplicatePurchaseOrderException(String number) {
        super("Purchase order already exists: " + number);
    }
}