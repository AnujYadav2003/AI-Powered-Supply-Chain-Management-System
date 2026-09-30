package com.supplychain.logistics_service.exception;

import java.util.UUID;

public class ShipmentNotFoundException extends RuntimeException {

    public ShipmentNotFoundException(UUID shipmentId) {
        super("Shipment not found with id: " + shipmentId);
    }
}