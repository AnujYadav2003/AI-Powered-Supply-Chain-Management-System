package com.supplychain.inventory_service.dto;

import com.supplychain.inventory_service.entity.InventoryStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResponse {

    private UUID id;

    private UUID productId;

    private UUID warehouseId;

    private Integer totalQuantity;

    private Integer reservedQuantity;

    private Integer availableQuantity;

    private Integer reorderLevel;

    private InventoryStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}