package com.supplychain.logistics_service.repository;

import com.supplychain.logistics_service.entity.Shipment;
import com.supplychain.logistics_service.entity.ShipmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShipmentRepository
        extends JpaRepository<Shipment, UUID> {

    Optional<Shipment> findByTrackingNumber(String trackingNumber);

    boolean existsByTrackingNumber(String trackingNumber);

    Page<Shipment> findByOrderId(
            UUID orderId,
            Pageable pageable
    );

    Page<Shipment> findByStatus(
            ShipmentStatus status,
            Pageable pageable
    );

    Page<Shipment> findBySourceWarehouseId(
            UUID warehouseId,
            Pageable pageable
    );
}