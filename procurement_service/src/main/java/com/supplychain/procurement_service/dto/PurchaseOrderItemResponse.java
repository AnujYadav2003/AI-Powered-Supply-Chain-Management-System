package com.supplychain.procurement_service.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderItemResponse {

    private UUID id;

    private UUID productId;

    private Integer quantity;

    private Integer receivedQuantity;

    private BigDecimal unitPrice;

    private BigDecimal totalPrice;
}