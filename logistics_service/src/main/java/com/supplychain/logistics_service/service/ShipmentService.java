package com.supplychain.logistics_service.service;

import com.supplychain.logistics_service.dto.ShipmentRequest;
import com.supplychain.logistics_service.dto.ShipmentResponse;
import com.supplychain.logistics_service.entity.ShipmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ShipmentService {

    ShipmentResponse createShipment(
            ShipmentRequest request
    );

    ShipmentResponse getShipmentById(
            UUID shipmentId
    );

    ShipmentResponse getShipmentByTrackingNumber(
            String trackingNumber
    );

    Page<ShipmentResponse> getAllShipments(
            Pageable pageable
    );

    Page<ShipmentResponse> getShipmentsByOrder(
            UUID orderId,
            Pageable pageable
    );

    Page<ShipmentResponse> getShipmentsByStatus(
            ShipmentStatus status,
            Pageable pageable
    );

    Page<ShipmentResponse> getShipmentsByWarehouse(
            UUID warehouseId,
            Pageable pageable
    );

    ShipmentResponse updateStatus(
            UUID shipmentId,
            ShipmentStatus status
    );

    void cancelShipment(UUID shipmentId);
}