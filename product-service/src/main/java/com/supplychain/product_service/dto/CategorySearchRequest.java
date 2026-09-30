package com.supplychain.product_service.dto;

import com.supplychain.product_service.entity.CategoryStatus;

public record CategorySearchRequest(
        String name,
        CategoryStatus status
) {
}