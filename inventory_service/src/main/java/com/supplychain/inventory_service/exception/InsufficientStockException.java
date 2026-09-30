package com.supplychain.inventory_service.exception;

public class InsufficientStockException
        extends RuntimeException {

    public InsufficientStockException() {

        super("Insufficient available stock");
    }
}