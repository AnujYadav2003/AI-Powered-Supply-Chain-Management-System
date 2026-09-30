package com.supplychain.warehouse_service.service;

import com.supplychain.warehouse_service.dto.*;
import com.supplychain.warehouse_service.entity.WarehouseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface WarehouseService {

    WarehouseResponse createWarehouse(WarehouseRequest request);

    WarehouseResponse getWarehouseById(UUID warehouseId);

    WarehouseResponse getWarehouseByCode(String code);

    Page<WarehouseResponse> getAllWarehouses(Pageable pageable);

    Page<WarehouseResponse> searchByCity(String city, Pageable pageable);

    Page<WarehouseResponse> getByStatus(WarehouseStatus status, Pageable pageable);

    WarehouseResponse updateWarehouse(UUID warehouseId, WarehouseUpdateRequest request);

    WarehouseResponse updateWarehouseStatus(UUID warehouseId, WarehouseStatus status);

    Page<WarehouseResponse> searchWarehouses(WarehouseSearchRequest request, Pageable pageable);
}