package com.supplychain.product_service.exceptions;
public class DuplicateSkuException extends RuntimeException {

    public DuplicateSkuException(String sku) {
        super("Product with SKU already exists: " + sku);
    }
}