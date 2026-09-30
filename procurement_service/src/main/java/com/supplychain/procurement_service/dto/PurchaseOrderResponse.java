package com.supplychain.procurement_service.dto;

import com.supplychain.procurement_service.entity.PurchaseOrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderResponse {

    private UUID id;

    private String purchaseOrderNumber;

    private UUID supplierId;

    private PurchaseOrderStatus status;

    private BigDecimal totalAmount;

    private LocalDateTime expectedDeliveryDate;

    private String notes;

    private List<PurchaseOrderItemResponse> items;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}