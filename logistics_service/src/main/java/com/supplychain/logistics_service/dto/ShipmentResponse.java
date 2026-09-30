package com.supplychain.logistics_service.dto;

import com.supplychain.logistics_service.entity.ShipmentStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentResponse {

    private UUID id;

    private String trackingNumber;

    private UUID orderId;

    private UUID sourceWarehouseId;

    private String destinationAddress;

    private String carrierName;

    private LocalDateTime estimatedDeliveryDate;

    private ShipmentStatus status;

    private List<ShipmentItemResponse> items;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}