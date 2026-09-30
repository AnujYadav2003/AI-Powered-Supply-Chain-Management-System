package com.supplychain.logistics_service.serviceImpl;

import com.supplychain.logistics_service.config.OrderClient;
import com.supplychain.logistics_service.config.ProductClient;
import com.supplychain.logistics_service.config.WarehouseClient;
import com.supplychain.logistics_service.dto.*;
import com.supplychain.logistics_service.entity.Shipment;
import com.supplychain.logistics_service.entity.ShipmentItem;
import com.supplychain.logistics_service.entity.ShipmentStatus;
import com.supplychain.logistics_service.entity.WarehouseStatus;
import com.supplychain.logistics_service.exception.ShipmentNotFoundException;
import com.supplychain.logistics_service.mapper.ShipmentMapper;
import com.supplychain.logistics_service.repository.ShipmentRepository;
import com.supplychain.logistics_service.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShipmentServiceImpl
        implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper shipmentMapper;

    private final OrderClient orderClient;
    private final ProductClient productClient;
    private final WarehouseClient warehouseClient;

    @Override
    @Transactional
    public ShipmentResponse createShipment(
            ShipmentRequest request
    ) {

        // 1. Validate Order
        OrderResponse order =
                orderClient.getOrderById(
                        request.getOrderId()
                );

        // Shipment should not be created for cancelled orders
        if ("CANCELLED".equalsIgnoreCase(order.getStatus())) {
            throw new IllegalStateException(
                    "Cannot create shipment for cancelled order: "
                            + request.getOrderId()
            );
        }

        // 2. Validate Warehouse
        WarehouseResponse warehouse =
                warehouseClient.getWarehouseById(
                        request.getSourceWarehouseId()
                );

        if (warehouse.getStatus() != WarehouseStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Warehouse is not active: "
                            + request.getSourceWarehouseId()
            );
        }

        // 3. Validate Products
        for (ShipmentItemRequest itemRequest :
                request.getItems()) {

            ProductResponse product =
                    productClient.getProductById(
                            itemRequest.getProductId()
                    );

            if (!"ACTIVE".equalsIgnoreCase(product.getStatus())) {
                throw new IllegalStateException(
                        "Product is not active: "
                                + itemRequest.getProductId()
                );
            }
        }

        // 4. Create shipment
        String trackingNumber =
                "TRK-" + System.currentTimeMillis();

        Shipment shipment = Shipment.builder()
                .trackingNumber(trackingNumber)
                .orderId(request.getOrderId())
                .sourceWarehouseId(
                        request.getSourceWarehouseId()
                )
                .destinationAddress(
                        request.getDestinationAddress()
                )
                .carrierName(request.getCarrierName())
                .estimatedDeliveryDate(
                        request.getEstimatedDeliveryDate()
                )
                .status(ShipmentStatus.CREATED)
                .items(new ArrayList<>())
                .build();

        for (ShipmentItemRequest itemRequest :
                request.getItems()) {

            ShipmentItem item =
                    ShipmentItem.builder()
                            .productId(itemRequest.getProductId())
                            .quantity(itemRequest.getQuantity())
                            .shipment(shipment)
                            .build();

            shipment.getItems().add(item);
        }

        Shipment savedShipment =
                shipmentRepository.save(shipment);

        return shipmentMapper.toResponse(savedShipment);
    }

    @Override
    public ShipmentResponse getShipmentById(
            UUID shipmentId
    ) {

        Shipment shipment =
                shipmentRepository.findById(shipmentId)
                        .orElseThrow(
                                () -> new ShipmentNotFoundException(
                                        shipmentId
                                )
                        );

        return shipmentMapper.toResponse(shipment);
    }

    @Override
    public ShipmentResponse getShipmentByTrackingNumber(
            String trackingNumber
    ) {

        Shipment shipment =
                shipmentRepository
                        .findByTrackingNumber(trackingNumber)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Shipment not found"
                                )
                        );

        return shipmentMapper.toResponse(shipment);
    }

    @Override
    public Page<ShipmentResponse> getAllShipments(
            Pageable pageable
    ) {

        return shipmentRepository
                .findAll(pageable)
                .map(shipmentMapper::toResponse);
    }

    @Override
    public Page<ShipmentResponse> getShipmentsByOrder(
            UUID orderId,
            Pageable pageable
    ) {

        return shipmentRepository
                .findByOrderId(orderId, pageable)
                .map(shipmentMapper::toResponse);
    }

    @Override
    public Page<ShipmentResponse> getShipmentsByStatus(
            ShipmentStatus status,
            Pageable pageable
    ) {

        return shipmentRepository
                .findByStatus(status, pageable)
                .map(shipmentMapper::toResponse);
    }

    @Override
    public Page<ShipmentResponse> getShipmentsByWarehouse(
            UUID warehouseId,
            Pageable pageable
    ) {

        return shipmentRepository
                .findBySourceWarehouseId(
                        warehouseId,
                        pageable
                )
                .map(shipmentMapper::toResponse);
    }

    @Override
    @Transactional
    public ShipmentResponse updateStatus(
            UUID shipmentId,
            ShipmentStatus status
    ) {

        Shipment shipment =
                shipmentRepository.findById(shipmentId)
                        .orElseThrow(
                                () -> new ShipmentNotFoundException(
                                        shipmentId
                                )
                        );

        shipment.setStatus(status);

        return shipmentMapper.toResponse(
                shipmentRepository.save(shipment)
        );
    }

    @Override
    @Transactional
    public void cancelShipment(
            UUID shipmentId
    ) {

        Shipment shipment =
                shipmentRepository.findById(shipmentId)
                        .orElseThrow(
                                () -> new ShipmentNotFoundException(
                                        shipmentId
                                )
                        );

        shipment.setStatus(
                ShipmentStatus.CANCELLED
        );

        shipmentRepository.save(shipment);
    }
}