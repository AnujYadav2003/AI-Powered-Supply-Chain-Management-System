package com.supplychain.product_service.dto;

import com.supplychain.product_service.entity.ProductStatus;

import java.util.UUID;

public record ProductSearchRequest(
        String name,
        String sku,
        UUID categoryId,
        String brand,
        ProductStatus status
) {
}