package com.supplychain.warehouse_service.dto;

import com.supplychain.warehouse_service.entity.WarehouseStatus;

public record WarehouseSearchRequest(

        String code,
        String name,
        String city,
        String state,
        String country,
        String pincode,
        WarehouseStatus status,
        Integer minCapacity,
        Integer maxCapacity,
        Integer minCurrentUtilization,
        Integer maxCurrentUtilization

) {}