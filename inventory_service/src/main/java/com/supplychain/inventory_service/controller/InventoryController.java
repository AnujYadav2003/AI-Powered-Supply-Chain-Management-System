package com.supplychain.inventory_service.controller;

import com.supplychain.inventory_service.dto.InventoryRequest;
import com.supplychain.inventory_service.dto.InventoryResponse;
import com.supplychain.inventory_service.dto.InventorySearchRequest;
import com.supplychain.inventory_service.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;


    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(
            @Valid @RequestBody InventoryRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        inventoryService.createInventory(request)
                );
    }


    @GetMapping("/{inventoryId}")
    public ResponseEntity<InventoryResponse> getInventoryById(
            @PathVariable UUID inventoryId
    ) {

        return ResponseEntity.ok(
                inventoryService.getInventoryById(
                        inventoryId
                )
        );
    }


    @GetMapping
    public ResponseEntity<Page<InventoryResponse>> getAllInventory(
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                inventoryService.getAllInventory(pageable)
        );
    }


    @PatchMapping("/{inventoryId}/add-stock")
    public ResponseEntity<InventoryResponse> addStock(
            @PathVariable UUID inventoryId,
            @RequestParam Integer quantity
    ) {

        return ResponseEntity.ok(
                inventoryService.addStock(
                        inventoryId,
                        quantity
                )
        );
    }


    @PatchMapping("/{inventoryId}/remove-stock")
    public ResponseEntity<InventoryResponse> removeStock(
            @PathVariable UUID inventoryId,
            @RequestParam Integer quantity
    ) {

        return ResponseEntity.ok(
                inventoryService.removeStock(
                        inventoryId,
                        quantity
                )
        );
    }


    @PatchMapping("/{inventoryId}/reserve")
    public ResponseEntity<InventoryResponse> reserveStock(
            @PathVariable UUID inventoryId,
            @RequestParam Integer quantity
    ) {

        return ResponseEntity.ok(
                inventoryService.reserveStock(
                        inventoryId,
                        quantity
                )
        );
    }


    @PatchMapping("/{inventoryId}/release")
    public ResponseEntity<InventoryResponse> releaseStock(
            @PathVariable UUID inventoryId,
            @RequestParam Integer quantity
    ) {

        return ResponseEntity.ok(
                inventoryService.releaseStock(
                        inventoryId,
                        quantity
                )
        );
    }

    @GetMapping("/search")
    public ResponseEntity<Page<InventoryResponse>> searchInventory(
            InventorySearchRequest request, Pageable pageable) {

        return ResponseEntity.ok(
                inventoryService.searchInventory(request, pageable)
        );
    }
}