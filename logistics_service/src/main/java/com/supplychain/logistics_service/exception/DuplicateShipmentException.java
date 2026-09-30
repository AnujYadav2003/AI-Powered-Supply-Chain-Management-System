package com.supplychain.logistics_service.exception;

public class DuplicateShipmentException extends RuntimeException {

    public DuplicateShipmentException(String trackingNumber) {
        super("Shipment already exists: " + trackingNumber);
    }
}