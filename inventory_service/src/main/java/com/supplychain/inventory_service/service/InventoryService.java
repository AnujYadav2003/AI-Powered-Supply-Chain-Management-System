package com.supplychain.inventory_service.service;

import com.supplychain.inventory_service.dto.InventoryRequest;
import com.supplychain.inventory_service.dto.InventoryResponse;
import com.supplychain.inventory_service.dto.InventorySearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface InventoryService {

    InventoryResponse createInventory(
            InventoryRequest request
    );

    InventoryResponse getInventoryById(
            UUID inventoryId
    );

    Page<InventoryResponse> getAllInventory(
            Pageable pageable
    );

    InventoryResponse addStock(
            UUID inventoryId,
            Integer quantity
    );

    InventoryResponse removeStock(
            UUID inventoryId,
            Integer quantity
    );

    InventoryResponse reserveStock(
            UUID inventoryId,
            Integer quantity
    );

    InventoryResponse releaseStock(
            UUID inventoryId,
            Integer quantity
    );

    Page<InventoryResponse> searchInventory(
            InventorySearchRequest request,
            Pageable pageable
    );
}