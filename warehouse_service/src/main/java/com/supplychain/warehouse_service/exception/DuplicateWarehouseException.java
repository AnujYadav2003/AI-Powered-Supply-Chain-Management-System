package com.supplychain.warehouse_service.exception;

public class DuplicateWarehouseException extends RuntimeException {

    public DuplicateWarehouseException(String code) {
        super("Warehouse already exists with code: " + code);
    }
}