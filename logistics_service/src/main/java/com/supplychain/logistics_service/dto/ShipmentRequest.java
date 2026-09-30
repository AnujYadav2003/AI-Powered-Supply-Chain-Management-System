package com.supplychain.logistics_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentRequest {

    @NotNull(message = "Order ID is required")
    private UUID orderId;

    @NotNull(message = "Source warehouse ID is required")
    private UUID sourceWarehouseId;

    @NotBlank(message = "Destination address is required")
    @Size(max = 1000)
    private String destinationAddress;

    @NotBlank(message = "Carrier name is required")
    private String carrierName;

    @Future(message = "Estimated delivery date must be in the future")
    private LocalDateTime estimatedDeliveryDate;

    @NotEmpty(message = "Shipment must contain at least one item")
    @Valid
    private List<ShipmentItemRequest> items;
}