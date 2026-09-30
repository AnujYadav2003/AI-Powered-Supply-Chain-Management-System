package com.supplychain.logistics_service.entity;

public enum ShipmentStatus {

    CREATED,
    PICKED_UP,
    IN_TRANSIT,
    ARRIVED_AT_WAREHOUSE,
    OUT_FOR_DELIVERY,
    DELIVERED,
    DELAYED,
    CANCELLED
}