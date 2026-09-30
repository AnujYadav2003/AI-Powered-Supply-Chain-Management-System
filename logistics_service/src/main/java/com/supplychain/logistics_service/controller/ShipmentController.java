package com.supplychain.logistics_service.controller;

import com.supplychain.logistics_service.dto.ShipmentRequest;
import com.supplychain.logistics_service.dto.ShipmentResponse;
import com.supplychain.logistics_service.entity.ShipmentStatus;
import com.supplychain.logistics_service.service.ShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShipmentResponse createShipment(
            @Valid @RequestBody ShipmentRequest request
    ) {
        return shipmentService.createShipment(request);
    }

    @GetMapping("/{shipmentId}")
    public ShipmentResponse getShipmentById(
            @PathVariable UUID shipmentId
    ) {
        return shipmentService.getShipmentById(shipmentId);
    }

    @GetMapping("/tracking/{trackingNumber}")
    public ShipmentResponse getByTrackingNumber(
            @PathVariable String trackingNumber
    ) {
        return shipmentService
                .getShipmentByTrackingNumber(trackingNumber);
    }

    @GetMapping
    public Page<ShipmentResponse> getAllShipments(
            @PageableDefault(size = 10)
            Pageable pageable
    ) {
        return shipmentService.getAllShipments(pageable);
    }

    @GetMapping("/order/{orderId}")
    public Page<ShipmentResponse> getByOrder(
            @PathVariable UUID orderId,
            @PageableDefault(size = 10)
            Pageable pageable
    ) {
        return shipmentService.getShipmentsByOrder(
                orderId,
                pageable
        );
    }

    @GetMapping("/status/{status}")
    public Page<ShipmentResponse> getByStatus(
            @PathVariable ShipmentStatus status,
            @PageableDefault(size = 10)
            Pageable pageable
    ) {
        return shipmentService.getShipmentsByStatus(
                status,
                pageable
        );
    }

    @GetMapping("/warehouse/{warehouseId}")
    public Page<ShipmentResponse> getByWarehouse(
            @PathVariable UUID warehouseId,
            @PageableDefault(size = 10)
            Pageable pageable
    ) {
        return shipmentService.getShipmentsByWarehouse(
                warehouseId,
                pageable
        );
    }

    @PatchMapping("/{shipmentId}/status")
    public ShipmentResponse updateStatus(
            @PathVariable UUID shipmentId,
            @RequestParam ShipmentStatus status
    ) {
        return shipmentService.updateStatus(
                shipmentId,
                status
        );
    }

    @PatchMapping("/{shipmentId}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelShipment(
            @PathVariable UUID shipmentId
    ) {
        shipmentService.cancelShipment(shipmentId);
    }
}